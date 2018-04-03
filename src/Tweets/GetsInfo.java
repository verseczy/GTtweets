package Tweets;

import twitter4j.*;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.List;

public class GetsInfo {
		public static void main(String[] args) {
			try {
	            Twitter twitter = new TwitterFactory().getInstance();
	            try {
	                // get request token.
	                // this will throw IllegalStateException if access token is already available
	                RequestToken requestToken = twitter.getOAuthRequestToken();
	                System.out.println("Got request token.");
	                System.out.println("Request token: " + requestToken.getToken());
	                System.out.println("Request token secret: " + requestToken.getTokenSecret());
	                AccessToken accessToken = null;

	                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	                while (null == accessToken) {
	                    System.out.println("Open the following URL and grant access to your account:");
	                    System.out.println(requestToken.getAuthorizationURL());
	                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
	                    String pin = br.readLine();
	                    try {
	                        if (pin.length() > 0) {
	                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
	                        } else {
	                            accessToken = twitter.getOAuthAccessToken(requestToken);
	                        }
	                    } catch (TwitterException te) {
	                        if (401 == te.getStatusCode()) {
	                            System.out.println("Unable to get the access token.");
	                        } else {
	                            te.printStackTrace();
	                        }
	                    }
	                }
	                System.out.println("Got access token.");
	                System.out.println("Access token: " + accessToken.getToken());
	                System.out.println("Access token secret: " + accessToken.getTokenSecret());
	            } catch (IllegalStateException ie) {
	                // access token is already available, or consumer key/secret is not set.
	                if (!twitter.getAuthorization().isEnabled()) {
	                    System.out.println("OAuth consumer key/secret is not set.");
	                    System.exit(-1);
	                }
	            }
	            User user = twitter.verifyCredentials();
	            List<Status> statuses = twitter.getHomeTimeline();
	            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
	            for (Status status : statuses) {
	            	    File output = new File("output.xml");
	            	    output.createNewFile();
	            	    BufferedWriter out = new BufferedWriter(new FileWriter(output));
	            	    out.write(status.getText());
	            	    out.flush();
	            	    out.close();
	                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
	                
	            }
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to get timeline: " + te.getMessage());
	            System.exit(-1);
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	            System.out.println("Failed to read the system input.");
	            System.exit(-1);
	        }
	    }
}
