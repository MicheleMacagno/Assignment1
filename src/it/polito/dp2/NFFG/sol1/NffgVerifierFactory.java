package it.polito.dp2.NFFG.sol1;

import it.polito.dp2.NFFG.NffgVerifier;

public class NffgVerifierFactory extends it.polito.dp2.NFFG.NffgVerifierFactory{

	
	
	@Override
	public NffgVerifier newNffgVerifier(){
		NffgVerifier2 nv=null;
		try {
			nv =new NffgVerifier2();
		} catch (Exception e) {
			System.out.println("Error in creating the Nffgverifier");
			e.printStackTrace();
		}
		return nv;
	}
}
