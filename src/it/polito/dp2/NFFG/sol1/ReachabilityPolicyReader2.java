package it.polito.dp2.NFFG.sol1;


import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.sol1.jaxb.Policy;

public class ReachabilityPolicyReader2 extends PolicyReader2 implements ReachabilityPolicyReader {

	private NodeReader nrSrc=null;
	private NodeReader nrDst=null;
	
	public ReachabilityPolicyReader2(Policy policy, NffgReader nffg,NodeReader nrSrc,NodeReader nrDst) {
		
		super(policy, nffg);
		
		this.nrSrc = nrSrc;
		this.nrDst = nrDst;
	}

	@Override
	public NodeReader getDestinationNode() {
		return nrDst;
	}

	@Override
	public NodeReader getSourceNode() {
		return nrSrc;
	}

	

}
