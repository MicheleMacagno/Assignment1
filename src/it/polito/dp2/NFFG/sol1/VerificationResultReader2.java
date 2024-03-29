package it.polito.dp2.NFFG.sol1;

import java.util.Calendar;

import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

public class VerificationResultReader2 implements VerificationResultReader {
	
	private PolicyReader pr=null;
	private Calendar verificationTime=null;
	private String message = null;
	private Boolean result = false;
	
	public VerificationResultReader2(PolicyReader pr,Calendar verificationTime,Boolean result,String message){
		this.pr=pr;
		this.verificationTime=verificationTime;
		this.message=message;
		this.result = result;
	}
	
	@Override
	public PolicyReader getPolicy() {
		return pr;
	}

	@Override
	public Boolean getVerificationResult() {
		return result;
	}

	@Override
	public String getVerificationResultMsg() {
		return message;
	}

	@Override
	public Calendar getVerificationTime() {
		return verificationTime;
	}

}
