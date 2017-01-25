package it.polito.dp2.NFFG.sol1;

import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;

public class NffgVerifierFactory extends it.polito.dp2.NFFG.NffgVerifierFactory{

	
	
	
	@Override
	public NffgVerifier newNffgVerifier() throws NffgVerifierException{
		NffgVerifier2 nv=null;
		try {
			nv =new NffgVerifier2();
		} catch (Exception e) {
			System.out.println("Error in creating the Nffgverifier");
			e.printStackTrace();
			throw new NffgVerifierException();
		} catch(Throwable t){
			System.out.println("Error in creating the Nffgverifier");
			throw new NffgVerifierException();
		}
		return nv;
	}
}
