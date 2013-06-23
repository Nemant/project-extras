import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import twitter4j.Status;

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
	        java.sql.Timestamp timestamp = new java.sql.Timestamp(tweet.getCreatedAt().getTime());
	        preparedStatement.setTimestamp(2, timestamp);
	        preparedStatement.setString(3, Long.toString(tweet.getUser().getId()));
	        preparedStatement.setString(4, tweet.getText());
	        preparedStatement.setString(5, tweet.getUser().getScreenName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
	}
	
	public void updateInInterval(EnumDataSet workingSet, Calendar start, Calendar end, int count, int uniqueUsers) {
		connection = Connector.getConnection();
		String query = "";
		try {
			if (workingSet == EnumDataSet.TWEETS) {
				query = statementTweetsInInterval; 
			} else if (workingSet == EnumDataSet.RETWEETS) {
				query = statementReTweetsInInterval;
			}
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
	        java.sql.Timestamp startTimestamp = new java.sql.Timestamp(start.getTime().getTime());
	        java.sql.Timestamp endTimestamp = new java.sql.Timestamp(end.getTime().getTime());
	        
	        preparedStatement.setTimestamp(1, startTimestamp);
	        preparedStatement.setTimestamp(2, endTimestamp);
	        preparedStatement.setInt(3, count);
	        preparedStatement.setInt(4, uniqueUsers);
	        
	        preparedStatement.executeUpdate();
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}
