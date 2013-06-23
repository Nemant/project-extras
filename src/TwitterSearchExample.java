import java.sql.SQLException;
//import java.util.List;
//
//import twitter4j.Query;
//import twitter4j.QueryResult;
//import twitter4j.Status;
//import twitter4j.Twitter;
//import twitter4j.TwitterException;
//import twitter4j.TwitterFactory;
import java.util.Calendar;

public class TwitterSearchExample {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
//		Connector connector = new Connector();
//		Store store = new Store();
//
//		Twitter twitter = new TwitterFactory().getSingleton();
//
//		Query queryTwitter = new Query("kenyan elections");
//		queryTwitter.setLang("en");
//		queryTwitter.setCount(100);
//
//		QueryResult result;
//
//		for (int i = 0; i < 1; i++) {
//			try {
//				result = twitter.search(queryTwitter);
//				List<Status> tweets = result.getTweets();
//				for (Status tweet : tweets) {
////					store.storeData(tweet);
//					System.out.println(tweet.getUser().getScreenName() + " : " + tweet.getText());
//				}	
//				queryTwitter = result.nextQuery();
//			} catch (TwitterException e) {
//				e.printStackTrace();
//			}
//		}
//
		Connector connector = new Connector();
		QueryDB querydb = new QueryDB();
		
		Metrics metric = new Metrics();
		
		metric.generateTimeLine(EnumDataSet.RETWEETS);

		
//		String[] users = new String[2];
//		users[0] = "34613288";
//		users[1] = "538031518";
//		
//		Calendar startTime = Calendar.getInstance();
//		startTime.set(2013, 2, 2, 21, 0);
//		Calendar endTime = Calendar.getInstance();
//		endTime.set(2013, 2, 3, 21, 0);
//		
//		metric.buildTimelineforUsers(users, startTime, endTime, 10);
//		
		// 2013-03-03 21:00 end time 
//		querydb.getTweetsForUserForInterval();
		System.out.println("adawds");
	}

}
