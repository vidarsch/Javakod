package se.wx3.fastagi.handlers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
//import java.io.File;
import java.sql.ResultSet;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.fastagi.AgiRequest;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import se.wx3.Wx3DatabaseConnection.Wx3DatabaseConnectionPool;
import se.wx3.fastagi.Wx3AgiScript;
import se.wx3.fastagi.Wx3FastAGIServer;
import se.wx3.logging.Wx3Logger;

public class GetIvrSendTextAgiScript extends Wx3AgiScript {
	private Wx3Logger logger = Wx3FastAGIServer.logger;

	public void service(AgiRequest request, AgiChannel channel) throws AgiException {
		try {
			String action_id = getParameter(request, "action_id");
			String caller_number = getParameter(request,"caller_number");
			String pbx = getParameter(request,"pbx");
			String data2 = "";
			String data1 = "";
			Boolean phone_number = true;
			
			String query = "";
			String query2 = "";
			
			PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
			PhoneNumber valid_caller_number = phoneUtil.parse(caller_number, "SE");
			
			
			if (phoneUtil.isValidNumber(valid_caller_number) && String.valueOf(phoneUtil.getNumberType(valid_caller_number)) == "MOBILE") {
				setVariable("Caller_True_Number","TRUE");
			} else {
				setVariable("Caller_True_Number","FALSE");
				phone_number = false;
			}
			
			if (phone_number) {
			
				query = "SELECT w.pbx_index,w.smsgwuser,w.smsaccountid,w.smsgwpass FROM welcomeinfo w WHERE pbx_index IN ("+pbx+")";
				query2 = "SELECT r.data1, r.data2 FROM ivr_block_action_response r WHERE r.id = "+action_id+"";
				
				ResultSet rs2 = Wx3DatabaseConnectionPool.executeQuery(query2);
				
				if (rs2.next()) {
					data1 = rs2.getString("data1");
					data2 = rs2.getString("data2");
				}
				
				ResultSet rs = Wx3DatabaseConnectionPool.executeQuery(query);
				
				String sms_content = data1;
				String sms_header = data2;
				
				if (sms_content != null && sms_header != null) {
					StringBuilder sb = new StringBuilder();
					String smsgwuser="";
					String smsgwpass="";
					String smsaccountid="";
					String sms_message = "";
					String dlr_value="";
					String smsnotifytext="";
					
					
					if (rs.next()) {
						smsgwuser=rs.getString("smsgwuser");
						smsgwpass=rs.getString("smsgwpass");
						smsaccountid=rs.getString("smsaccountid");
						sms_message = sms_content;
						dlr_value="0";
						smsnotifytext=sms_header; 
						
						
						logger.getLogger().finer(smsaccountid);
						logger.getLogger().finer(smsgwuser);
						logger.getLogger().finer(sms_message);
						}
					
					try {
						smsaccountid = URLEncoder.encode(smsaccountid, "ISO-8859-1");
						smsgwuser = URLEncoder.encode(smsgwuser, "ISO-8859-1");
						smsgwpass = URLEncoder.encode(smsgwpass, "ISO-8859-1");
						sms_message = URLEncoder.encode(sms_message, "ISO-8859-1");
						smsnotifytext = URLEncoder.encode(smsnotifytext, "ISO-8859-1");
						
					} catch (UnsupportedEncodingException e1) {
						logger.getLogger().severe("Error, sms command failed!");
						logger.getLogger().severe(e1.getMessage());
						logger.getLogger().warning(Wx3Logger.formatException(e1));
						return;
					}
					
					sb.append("https://smsgw.wx3.se/sendsms.php?");
					sb.append("to=").append(caller_number);
					sb.append("&accountid=").append(smsaccountid);
					sb.append("&username=").append(smsgwuser);
					sb.append("&password=").append(smsgwpass);
					sb.append("&msg=").append(sms_message);
					sb.append("&dlr=").append(dlr_value);
					sb.append("&from=").append(smsnotifytext);
					
					String urlString=sb.toString();
					
					URL url = new URL(urlString);
					try {
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setRequestMethod("GET");
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						in.close();
						
					}
					catch(Exception e3) {
						
						logger.getLogger().severe("Error opening URL for sms gateway");
						logger.getLogger().severe(e3.toString());
						
					}
					
					
					logger.getLogger().finer(urlString);
					
		         }
			
			
			}
		} catch(AgiHangupException ahe) {
		} catch(Exception e) {
			logger.getLogger().warning("Error in handler: " + e.getMessage());
			logger.getLogger().warning(Wx3Logger.formatException(e));
		}
	}
}