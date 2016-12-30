package it.polito.dp2.NFFG.sol1;


import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;
import it.polito.dp2.NFFG.sol1.jaxb.Policy;
import it.polito.dp2.NFFG.sol1.jaxb.Verification;

public class PolicyReader2 extends NamedEntityReader2 implements PolicyReader {

	private NffgReader nffgReader=null;
	private Boolean positive=false;
	private VerificationResultReader verificationResult=null;
	
	public PolicyReader2(Policy policy, NffgReader nffg){
		super(policy.getName());
		this.nffgReader=nffg;
		positive = policy.isPositivity();
		Verification ver = policy.getVerification();
		
		if(ver!=null){
			this.verificationResult = new VerificationResultReader2(
					this,
					ver.getVerificationTime().toGregorianCalendar(),
					ver.isResult(),
					ver.getMessage()
					);
		}
	}

	@Override
	public NffgReader getNffg() {
		return nffgReader;
	}

	@Override
	public VerificationResultReader getResult() {
		//it can return null if the verification data are not existing
		return verificationResult;
	}

	@Override
	public Boolean isPositive() {
		return positive;
	}

}
