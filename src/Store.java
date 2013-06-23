import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import twitter4j.Status;
import twitter4j.User;

public class Store {

	private static Connection connection;
	private String statementTweets = "INSERT INTO \"FinalProject\".\"Tweets\"(\"TweetID\", \"DateTime\", \"User\", \"Blog\", \"UserName\") VALUES(?, ?, ?, ?, ?)";
	private String statementReTweets = "INSERT INTO \"FinalProject\".\"ReTweets\"(\"TweetID\", \"DateTime\", \"User\", \"Blog\", \"UserName\", \"OriginalTweetID\") VALUES(?, ?, ?, ?, ?, ?)";
	private static String statementTweetsInInterval = "INSERT INTO \"FinalProject\".\"TweetsInInterval\" (\"DateTimeStart\", \"DateTimeFinish\", \"UniqueTweets\", \"UniqueUsers\") VALUES (?, ?, ?, ?);";
	private static String statementReTweetsInInterval = "INSERT INTO \"FinalProject\".\"ReTweetsInInterval\" (\"DateTimeStart\", \"DateTimeFinish\", \"UniqueReTweets\", \"UniqueUsers\") VALUES (?, ?, ?, ?);";

	public Store() {
		connection = Connector.getConnection();
	}

	public void storeData(Status tweet) {
		if (tweet.isRetweet()) {
			storeReTweet(tweet);
		} else {
			storeTweet(tweet);
		}
	}

	public void storeTweet(Status tweet) {
		PreparedStatement stm = prepareStatement(tweet, statementTweets);
		try {
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void storeReTweet(Status tweet) {
		PreparedStatement stm = prepareStatement(tweet, statementReTweets);
		try {
			stm.setString(6, Long.toString(tweet.getRetweetedStatus().getId()));
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Retweet: " + e.getMessage());
		}
	}

	private PreparedStatement prepareStatement(Status tweet, String statement) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(statement);
			preparedStatement.setString(1, Long.toString(tweet.getId()));
			java.sql.Timestamp timestamp = new java.sql.Timestamp(tweet
					.getCreatedAt().getTime());
			preparedStatement.setTimestamp(2, timestamp);
			preparedStatement.setString(3,
					Long.toString(tweet.getUser().getId()));
			preparedStatement.setString(4, tweet.getText());
			preparedStatement.setString(5, tweet.getUser().getScreenName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
	}

	public void updateInInterval(EnumDataSet workingSet, Calendar start,
			Calendar end, int count, int uniqueUsers) {
		connection = Connector.getConnection();
		String query = "";
		try {
			if (workingSet == EnumDataSet.TWEETS) {
				query = statementTweetsInInterval;
			} else if (workingSet == EnumDataSet.RETWEETS) {
				query = statementReTweetsInInterval;
			}

			PreparedStatement preparedStatement = connection
					.prepareStatement(query);
			java.sql.Timestamp startTimestamp = new java.sql.Timestamp(start
					.getTime().getTime());
			java.sql.Timestamp endTimestamp = new java.sql.Timestamp(end
					.getTime().getTime());

			preparedStatement.setTimestamp(1, startTimestamp);
			preparedStatement.setTimestamp(2, endTimestamp);
			preparedStatement.setInt(3, count);
			preparedStatement.setInt(4, uniqueUsers);

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRetweetsForTweet(HashMap<String, Status> originalTweets, HashMap<String, Integer> retweetsForOriginalTweet) {
		Iterator it = originalTweets.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
			String tweetID = (String) pairs.getKey();
			Status value = (Status) pairs.getValue();

			ResultSet rs = QueryDB.getRetweetsForTweet(tweetID);

			int totalRetweets = 0;
			boolean recordExists = false;
			try {
				if (rs.next()) {
					recordExists = true;
					totalRetweets = rs.getInt(4);
				}

				totalRetweets = totalRetweets + retweetsForOriginalTweet.get(tweetID);

				if (recordExists) {
					rs.updateInt(4, totalRetweets);
					rs.updateRow();
				} else {
					rs.moveToInsertRow();
					rs.updateString("TweetID", tweetID);
					java.sql.Timestamp timestamp = new java.sql.Timestamp(value.getCreatedAt().getTime());
					rs.updateTimestamp("DateTime", timestamp);
					rs.updateString("User",Long.toString(value.getUser().getId()));
					rs.updateInt("Total No. of Retweets", totalRetweets);
					rs.updateString("Blog", value.getText());
					rs.insertRow();
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			it.remove();
		}
	}
	
	public void updateRetweetsForUser(HashMap<String, User> originalUserInfo, HashMap<String, Integer> originalUserRetweetsCount) {
		Iterator it = originalUserInfo.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
			String userID = (String) pairs.getKey();
			User value = (User) pairs.getValue();

			ResultSet rs = QueryDB.getRetweetsForUser(userID);

			int totalRetweets = 0;
			try {
				if (rs.next()) {
					totalRetweets = rs.getInt(2);
					totalRetweets = totalRetweets + originalUserRetweetsCount.get(userID);
					rs.updateInt(2, totalRetweets);
					rs.updateInt(4, totalRetweets / rs.getInt(3));
					rs.updateFloat(10, (float)Math.round((totalRetweets / rs.getInt(6))*1000) / 1000); // Use dedcimals
					rs.updateRow();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			it.remove();
		}
	}
	
	public void storeUser(User value){
		String userID = Long.toString(value.getId());
		
		ResultSet rs = QueryDB.getRetweetsForUser(userID);
		try {
			if (rs.next()) {
				System.out.println("yo!" + rs.getInt(3));
				rs.updateInt(3, rs.getInt(3) + 1);
				rs.updateInt(4, rs.getInt(2) / rs.getInt(3));
				rs.updateRow();
			} else {
				rs.moveToInsertRow();
				rs.updateString("User", Long.toString(value.getId()));
				rs.updateInt("Total Retweets", 0);
				rs.updateInt("Total No. Of Tweets", 1);
				rs.updateInt("AverageRetweets", 0);
				rs.updateString("Username", value.getScreenName());
				rs.updateInt("Followers", value.getFollowersCount());
				rs.updateInt("Followees", value.getFriendsCount());
				rs.updateString("Location", value.getLocation());
				rs.updateString("Timezone", value.getTimeZone());
				rs.updateInt("Retweet to Followers Ratio", 0);
				rs.insertRow();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
