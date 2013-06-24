import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public class TwitterStreamingExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connector connector = new Connector();
		
		StatusListener listener = new StatusListener(){
			Metrics metric = new Metrics();
			QueryDB queryDB = new QueryDB();
			
	        public void onStatus(Status tweet) {
	        	metric.processStatus(tweet);
	        }
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	        }
			public void onScrubGeo(long arg0, long arg1) {}
			public void onStallWarning(StallWarning stallWarning) {}
	    };
	    
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	    
	    FilterQuery filterQuery = new FilterQuery();
	    String keywords[] = {"arsenal"};
	    filterQuery.track(keywords);

	    twitterStream.addListener(listener);
	    twitterStream.filter(filterQuery);

	}

}
