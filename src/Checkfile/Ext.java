package com.ae.intg;

import com.ae.intg.models.DbRequestPayload;
import com.ae.intg.models.DbResponse;
import com.ae.intg.models.DbStatus;
import com.ae.intg.models.Values;
import com.automationedge.common.ext.integration.*;
import com.automationedge.enums.AutomationRequestStatus;
import com.automationedge.enums.AutomationResponseCode;
import com.automationedge.exception.AEUtilsException;
import com.automationedge.model.AutomationRequest;
import com.automationedge.model.AutomationRequestStatusUpdate;
import com.automationedge.model.AutomationResponse;
import com.automationedge.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@AeIntgerationType
public class newChatbotControll implements IAeIntegrationType {
    private static final Logger LOGGER = LoggerFactory.getLogger(newChatbotControll.class);
    private final HashMap<String, IntgConfParameter> configurationParameters = new HashMap<>();
    private Connection c = null;
    private Statement stmt = null;
    private String servername = null;
    private String drivername = null;
    private ResultSet res ;
    private String accessToken = null;
    
    private String sample = null;

    public void init(IntgTypeConfiguration intgTypeConf) throws AeIntegrationTypeException {
        LOGGER.debug("<DB> Entered init method");
        LOGGER.info("<DB>  Entered init method");
        try {
            populateConfParams(intgTypeConf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        try {
            dbConnection(intgTypeConf);
        } catch (AeIntegrationTypeException e) {
            LOGGER.error("Error", e);

        }

        LOGGER.debug("<DB> Exited init method");
    }

    public boolean testConnection(IntgTypeConfiguration intgTypeConf) throws AeIntegrationTypeException {
        LOGGER.debug("<DB> Entered testConnection method");

        try {
            populateConfParams(intgTypeConf);
        
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        try {
            dbConnection(intgTypeConf);
 
            
        } catch (AeIntegrationTypeException e) {
            LOGGER.error("<DB> Test connection failed ", e);
            return false;
        }
        LOGGER.debug("<DB> Exiting testConnection method");
        return true;
    }

    public void dbConnection(IntgTypeConfiguration intgTypeConf) throws AeIntegrationTypeException {
        LOGGER.debug("<DB> selected server: "+servername);
        System.out.println("Into dbConnection method");
            drivername = "org.postgresql.Driver";
            String URL = null;
            System.out.println("Test Connectionnn Succesfull");
            try {
                
                    URL = "jdbc:postgresql" + "://"
                            + configurationParameters.get(Constants.DB_Host).getValue() + ":"
                            + (configurationParameters.get(Constants.DB_Port).getValue()) + "/"
                            + (configurationParameters.get(Constants.DB_DBNAME).getValue());
                
//                    System.out.println("url=> "+URL);
                    
                Class.forName(drivername);
                c = DriverManager.getConnection(URL, (configurationParameters.get(Constants.DB_USER_KEY).getValue()),
                        (configurationParameters.get(Constants.DB_PASSWORD_KEY).getValue()));
                
//                System.out.println("connection => " +c);
            } catch (Exception e) {
                LOGGER.debug("<DB> Login failure, {}", e);
                throw new AeIntegrationTypeException(e);
           }
    }
    
    private ResultSet query() throws AeIntegrationTypeException, SQLException{
    	String query = configurationParameters.get(Constants.DB_Query).getValue();
    	try {
    		stmt = c.createStatement();
    		res = stmt.executeQuery(query);
			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.debug("<DB> Query failure, {}", e);
			throw new AeIntegrationTypeException(e);
		}
    	System.out.println("postgres url => "+res);
		return res;
    }

    private void populateConfParams(IntgTypeConfiguration integrationTypeConf) throws Exception {
        LOGGER.debug("<DB> Entered populateConfParams method");
        if (integrationTypeConf.getConfigParams() == null) {
            throw new AeIntegrationTypeException("<DB> Integration Type configuration parameters is null");
        }

        LOGGER.debug("<DB> Configuration parameters received are: ");
        configurationParameters.clear();
        for (IntgConfParameter parameter : integrationTypeConf.getConfigParams()) {
            configurationParameters.put(parameter.getName(), parameter);
           

        }

        File f = new File(configurationParameters.get(Constants.DB_FilePath).getValue());
       
        sample= readFileAsString(configurationParameters.get(Constants.DB_FilePath).getValue());

        //System.out.println(servername);
        if ((configurationParameters.get(Constants.DB_USER_KEY) == null)
                || (configurationParameters.get(Constants.DB_PASSWORD_KEY) == null)
                || (configurationParameters.get(Constants.DB_Port) == null)
                || (configurationParameters.get(Constants.DB_DBNAME) == null)
                || (configurationParameters.get(Constants.DB_Query) == null)
                || (configurationParameters.get(Constants.DB_FilePath) == null)
                || (configurationParameters.get(Constants.DB_Host) == null)) {
            throw new AeIntegrationTypeException(
                    "<DB> The required configuration parameters are not present i.e." + Constants.DB_USER_KEY + ", "
                            + Constants.DB_PASSWORD_KEY + "," + Constants.DB_Port + "," + Constants.DB_DBNAME + " , "
                            + Constants.DB_Query + "," + Constants.DB_FilePath + " and " + Constants.DB_Host);
        }
        if (!f.exists()) {
            throw new AeIntegrationTypeException("<DB> File not present.");
        }
        if(sample.isEmpty())
        {
            throw new AeIntegrationTypeException("<DB> File is empty.");
        }
         
        LOGGER.debug("<DB> Exited populateConfParams method");
    }

    public void cleanup() throws AeIntegrationTypeException {
        // TODO Auto-generated method stub

    }

    public void downloadRequestAttachments(AutomationRequest arg0, File arg1) throws AeIntegrationTypeException {
        // TODO Auto-generated method stub

    }

    public List<AutomationRequest> poll() throws AeIntegrationTypeException {
        LOGGER.debug("<DB> Entered poll method");
        System.out.println("<DB> Entered poll method");
        List<AutomationRequest> AERequestList = new ArrayList<>();
        ResultSet DBFlag = null ;
        try {
//            DbRequestPayload dbrequestPayload = null;
//			String sourceID = null;
			DBFlag = query();
//			updateQuery(dbrequestPayload, sourceID); 
            populateAutomationRequestList(AERequestList, DBFlag);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            LOGGER.debug("<DB> Query failure, {}", e1);
        }
        LOGGER.debug("<DB> Exiting poll method");
        return AERequestList;
    }

       private void populateAutomationRequestList(List<AutomationRequest> aeRequestList, ResultSet res) throws Exception {
        LOGGER.debug("<DB> Entered populateAutomationRequestList method");
        System.out.println("<DB> Entered populateAutomationRequestList method");
        // AutomationRequest aeRequest;
        AutomationRequest aeRequest = new AutomationRequest();
        while(res.next())
        {
        	String workflow_payload = res.getString("workflow_payload");
        	JSONObject json = new JSONObject(workflow_payload);
        	aeRequest = JsonUtils.deserialize(json.toString(), AutomationRequest.class);
			aeRequestList.add(aeRequest);
			System.out.println("aeRequest => "+aeRequest);
//			System.out.println("id : "+res.getInt(1)+"\n endPoint : "+res.getString(2)+"\n additionalInfo : "+res.getString(3)+" \n wfPayload : "+res.getString(4)+" \n state : "+res.getString(5)+" \n DnT : "+res.getString(6));
			System.out.println("aeRequest result => " + new Gson().toJson(aeRequest));
        }
        LOGGER.info("<DB> Requests response: AE Requests Count: {}", aeRequestList.size());
		LOGGER.info("<DB> Exited populateAutomationRequestList method");
       }
  
    public static String readFileAsString(String fileName) throws Exception {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        //System.out.println("File"+data);
        return data;
    }
    @Override
	public List<Long> updateStatus(List<AutomationRequestStatusUpdate> requestStatus)
			throws AeIntegrationTypeException {
		LOGGER.debug("<DB> Entered updateStatus method");
		List<DbResponse> rRequestList = requestStatus.stream()
				.map(aeStatus -> new com.ae.intg.models.DbResponse())
				.collect(Collectors.<DbResponse>toList());
		LOGGER.debug("<DB> Exiting updateStatus method");
		return updateRequestStatus(rRequestList, null);
	}
    private List<Long> updateRequestStatus(List<DbResponse> rResponseList, File outputAttachment) throws AeIntegrationTypeException {
		LOGGER.debug("<DB> Entered updateRequestStatus method");
		List<Long> updateFailedRequestIds = new ArrayList<>();
		try {
			for(DbResponse rResponse : rResponseList) {
				Long aeRequestID = rResponse.getAutomationRequestId();
				String sourceID = rResponse.getSourceId();
				boolean DBResponse = updateDb(rResponse, aeRequestID, sourceID);
			}		
		} catch (Exception e) {
			throw new AeIntegrationTypeException(e);
		}
		LOGGER.debug("<DB> Exiting updateRequestStatus method");
		return updateFailedRequestIds;
	}
    private boolean updateDb(DbResponse rResponse, Long aeRequestID, String sourceID)
			throws AEUtilsException, JsonProcessingException, IOException, AeIntegrationTypeException {
		LOGGER.debug("<ROD> Entered updateRemedyForm method");
		LOGGER.debug("<ROD> AutomationEdge Request ID {}, Source ID {}, Update status URL {}", aeRequestID, sourceID);
			
		DbRequestPayload dbRequestPayLoad = new DbRequestPayload();
		Values values = new Values();
		
		if ((rResponse.getStatus() == AutomationRequestStatus.New) || (rResponse.getStatus() == AutomationRequestStatus.InProgress)) {
			values.setDescription("Automation request id is " + rResponse.getAutomationRequestId());
			values.setStatus(DbStatus.RUNNING.toString());
			System.out.println("In New/Running State ---- Automation request id is " + rResponse.getAutomationRequestId());
		} else if (rResponse.getStatus() == AutomationRequestStatus.Completed) {
			values.setDescription(rResponse.getMessage());
			values.setStatus(DbStatus.SUCCESS.toString());
			System.out.println(rResponse.getMessage()+"  Status : Completed");
		} else {
			values.setDescription(rResponse.getMessage());
			values.setStatus(DbStatus.ERROR.toString());
			System.out.println("Status: Error Occured"+ DbStatus.ERROR.toString());
		}
		dbRequestPayLoad.setValues(values);
		LOGGER.debug("<ROD> AutomationEdge Request ID: {}, Source ID: {}, Update Request Payload: {}", aeRequestID, sourceID, JsonUtils.serialize(dbRequestPayLoad));
		updateQuery(dbRequestPayLoad, sourceID);
		LOGGER.debug("<DB> Exit from updateDb method");
		return true;
		
		
	}
    private void populateFailedRequestIdList(List<Long> updateFailedRequestIds, DbResponse rResponse,
			Long aeRequestID, String sourceID) {
		LOGGER.debug("<DB> Entered populateFailedRequestIdList method");
		LOGGER.error("<DB> AutomationEdge Request ID {}, Source ID {} : Failed to update status", aeRequestID, sourceID);
		updateFailedRequestIds.add(rResponse.getAutomationRequestId());
		LOGGER.debug("<DB> Exited populateFailedRequestIdList method");
	}
    
    public void updateQuery(DbRequestPayload dbrequestPayload, String sourceID) throws AeIntegrationTypeException{
    	LOGGER.debug("<DB> Entered into update query");
    	System.out.println("<DB> Entered into update query");
    	try {
			String state = dbrequestPayload.getValues().getStatus();
			String response = dbrequestPayload.getValues().getResponse();
		
			c.getMetaData();
			String query = "UPDATE ic_chatbot_details SET state = ?, response = ? where id = ?";
			
			PreparedStatement pstmt = c.prepareStatement(query);
			pstmt.setInt(3, Integer.parseInt(sourceID));
			pstmt.setString(1, "closed");
			pstmt.setString(2, response);
			pstmt.executeQuery();
			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.debug("<DB> Update query failure, {}", e);
			throw new AeIntegrationTypeException(e);
		}    	
    }

	@Override
	public boolean updateResponse(AutomationResponse automationResponse, File outputAttachments)
			throws AeIntegrationTypeException {
		LOGGER.debug("<DB> Entered updateResponse method");
		LOGGER.debug("<DB> Automation Response {}, {}", automationResponse.getWorkflowResponse(), automationResponse.getErrorDetails());
		DbResponse rfResponse = new DbResponse();
		rfResponse.setAutomationRequestId(automationResponse.getAutomationRequestId());
		rfResponse.setSource(automationResponse.getSource());
		rfResponse.setSourceId(automationResponse.getSourceId());
		if(automationResponse.isSuccess()) {
			rfResponse.setMessage(automationResponse.getWorkflowResponse().getMessage());
		} else {
			rfResponse.setMessage(automationResponse.getWorkflowResponse().getError());
		}

		if (automationResponse.getResponseCode() == AutomationResponseCode.Complete) {
			rfResponse.setStatus(AutomationRequestStatus.Completed);
			System.out.println("Status : Completed");
		} else {
			LOGGER.info("<DB> By default, setting status as Failure, actual status of workflow is {}", automationResponse.getResponseCode());
			rfResponse.setStatus(AutomationRequestStatus.Failure);
			System.out.println("Status : Failure");
		}
		
		rfResponse.setOutputParameters(automationResponse.getWorkflowResponse().getOutputParameters());
		List<DbResponse> responseList = new ArrayList<>();
		responseList.add(rfResponse);
		
		LOGGER.debug("<DB> Exiting updateResponse method");
		return updateRequestStatus(responseList, outputAttachments).isEmpty();
	}
}
