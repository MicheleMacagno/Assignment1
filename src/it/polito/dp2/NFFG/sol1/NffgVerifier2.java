package it.polito.dp2.NFFG.sol1;

import java.io.File;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.sol1.jaxb.Nffgs;
public class NffgVerifier2 implements it.polito.dp2.NFFG.NffgVerifier {

	Set<NffgReader> setNffgReader2=null;
	Set<PolicyReader> setPolicyReader2=null;
	
	//add the element to read the XML document
	JAXBContext jc;
	Unmarshaller u;
	Nffgs o;
	
	/**
	 * Initialize set of items to store the interested informations
	 * Throws an NffgVerifierException if an implementation of if can't be created
	 */
	public NffgVerifier2() throws NffgVerifierException{
		try{
			setNffgReader2 		= new LinkedHashSet<NffgReader>(0);
			setPolicyReader2 	= new LinkedHashSet<PolicyReader>(0);
			prepareUnmarshal();
		}catch(Exception e){
			e.printStackTrace();
			throw new NffgVerifierException();
		}
	}
	
	@Override
	public NffgReader getNffg(String name) {
		for(NffgReader nr : setNffgReader2){
			if(nr.getName().equals(name)){
				return nr;
			}
		}
		//in case nothing found it must return null		
		return null;
	}

	@Override
	public Set<NffgReader> getNffgs() {
		//it must never return null
		return setNffgReader2;
	}

	@Override
	public Set<PolicyReader> getPolicies() {
		//it must never return null
		return setPolicyReader2;
	}

	@Override
	public Set<PolicyReader> getPolicies(String namenffg) {
		Set<PolicyReader> toReturn = 
				setPolicyReader2.stream().filter(p->{
			return(	p.getNffg().getName().equals(namenffg) );
		}).collect(Collectors.toSet());
		
		//in case nothing found, return null - by definition
		if(toReturn.size()==0){
			return null;
		}
		else{
			return toReturn;
		}
	}

	@Override
	public Set<PolicyReader> getPolicies(Calendar verificationtime) {
		Set<PolicyReader> toReturn =
				setPolicyReader2.stream().filter(p->{
						//verify if verification data exists for the following policy
						if(p.getResult()==null) {
							return false;
						}
						//if exist, verify if the verification date is greater than the current one
						return((p.getResult().getVerificationTime().compareTo(verificationtime))> 0 );
		}).collect(Collectors.toSet());
		
		//in case nothing found, return null - by definition
		if(toReturn.size()==0){
			return null;
		}
		else{
			return toReturn;
		}
	}
	
	private void prepareUnmarshal() throws NffgVerifierException{
		
		
		try {
			jc = JAXBContext.newInstance("it.polito.dp2.NFFG.sol1.jaxb");
			SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			u = jc.createUnmarshaller();
			try{
				u.setSchema(sf.newSchema(new File("xsd/nffgInfo.xsd")));
				
			}catch(Exception e){
				e.printStackTrace();
				throw new NffgVerifierException();
			}
			o =(Nffgs)u.unmarshal(new File(System.getProperty("it.polito.dp2.NFFG.sol1.NffgInfo.file")));
			
			if(o==null){
				throw new NffgVerifierException();
			}
			createNffgReader();
			
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new NffgVerifierException();
		}catch (Exception e){
			e.printStackTrace();
			throw new NffgVerifierException();
		}
		
		
	}
	
	//NFFG READER
	private void createNffgReader(){
		o.getNffg().forEach(nffg->{
			//Name
			NffgReader2 tmpNffg = new NffgReader2(nffg);
			setNffgReader2.add(tmpNffg);
		});
		
		
		
		//extract the correspondent NffgReader
		o.getNffg().forEach(nffg ->{
			NffgReader nr=
				setNffgReader2.stream().filter(a->{
					return a.getName().equals(nffg.getName());
				}).collect(Collectors.toList()).get(0);
			
			nffg.getPolicy().stream().forEach(policy -> {
							NodeReader nrSrc = nr.getNodes().stream().filter(n->{
								return(	n.getName().equals(policy.getSrc())	);
								
							}).collect(Collectors.toList()).get(0);
							
							NodeReader nrDst =  nr.getNodes().stream().filter(n->{
								return(	n.getName().equals(policy.getDst())	);
								
							}).collect(Collectors.toList()).get(0);
				
							if(policy.getTraversal()==null){
								//REACHABILITY POLICY
								ReachabilityPolicyReader2 rpr = new ReachabilityPolicyReader2(policy, nr, nrSrc, nrDst);
								setPolicyReader2.add(rpr);
							}
							else{
								//TRAVERSAL POLICY
								TraversalPolicyReader2 tpr = new TraversalPolicyReader2(policy, nr, nrSrc, nrDst);
								setPolicyReader2.add(tpr);
							}
			
			});
		});
		
		
		
		
	}

}