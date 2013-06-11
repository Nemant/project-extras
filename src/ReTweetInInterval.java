import java.util.Calendar;
import java.util.Date;

import org.jfree.data.time.TimePeriod;

public class ReTweetInInterval implements TimePeriod  {

	private String tweetID;
	private Calendar beginInterval = Calendar.getInstance();
	private Calendar endInterval = Calendar.getInstance();;
	private int noOfReTweets;
	
	public ReTweetInInterval(String tweetID, Calendar beginInterval, Calendar endInterval) {
		this.tweetID = tweetID;
		this.beginInterval.setTimeInMillis(beginInterval.getTimeInMillis());
		this.endInterval.setTimeInMillis(endInterval.getTimeInMillis());
	}
	
	public String getTweetID() {
		return tweetID;
	}
	
	public void setTweetID(String tweetID) {
		this.tweetID = tweetID;
	}
	
	public Calendar getBeginInterval() {
		return beginInterval;
	}
	
	public void setBeginInterval(Calendar beginInterval) {
		this.beginInterval = beginInterval;
	}
	
	public Calendar getEndInterval() {
		return endInterval;
	}
	
	public void setEndInterval(Calendar endInterval) {
		this.endInterval = endInterval;
	}
	
	public int getNoOfReTweets() {
		return noOfReTweets;
	}
	
	public void setNoOfReTweets(int noOfReTweets) {
		this.noOfReTweets = noOfReTweets;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof String))
			  return false;			
		String tweetID = (String) o;
		
		return this.tweetID.equals(tweetID);
	}
	
	public String toString() {
		return "TweetID: " + tweetID + " Interval Start: " + beginInterval.getTime().toString() + " Interval End: " + endInterval.getTime().toString() + " NRT: " + noOfReTweets;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getEnd() {
		return endInterval.getTime();
	}

	@Override
	public Date getStart() {
		return beginInterval.getTime();
	}
	
}
