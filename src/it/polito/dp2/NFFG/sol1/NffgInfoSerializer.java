package it.polito.dp2.NFFG.sol1;
import it.polito.dp2.NFFG.sol1.jaxb.*;
import it.polito.dp2.NFFG.*;


import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import javax.xml.datatype.DatatypeConfigurationException;




public class NffgInfoSerializer {
	private it.polito.dp2.NFFG.NffgVerifier monitor;
	
	private Nffgs nffgs=null;
	
	Nffgs myNffgs;
	
	/**
	 * Default constructror
	 * @throws NffgVerifierException 
	 */
	public NffgInfoSerializer() throws NffgVerifierException {
		it.polito.dp2.NFFG.NffgVerifierFactory factory=null;
		
		try{
			factory = it.polito.dp2.NFFG.NffgVerifierFactory.newInstance();
		}catch(java.lang.Error e){
			e.printStackTrace();
			throw new NffgVerifierException();
		}
		
		try{
			monitor = factory.newNffgVerifier();
		}catch(Exception e){
			e.printStackTrace();
			throw new NffgVerifierException();
		}
		
	}
	
	public NffgInfoSerializer(NffgVerifier monitor) {
		super();
		this.monitor = monitor;
	}
	
	/**
	 * method used to initialize the unmarshaller
	 * TODO: add the possibility to modify the output file name
	 * TODO: verify why IOException catching rise an errors
	 */
	public void prepareToMarshal(){
			
			Set<NffgReader> set = monitor.getNffgs();
			Nffgs nffgs = new Nffgs();
			
//NFFG
			for (NffgReader nffg_r: set) {
					
					//extract the nffg and save relative informations
					Nffg nffg = new Nffg();
					nffg.setName(nffg_r.getName());
					
					XMLGregorianCalendar dataConverted=null;
					dataConverted = convertCalendar(nffg_r.getUpdateTime());
					if( dataConverted != null){
						nffg.setLastUpdate(dataConverted);
					}
					else{
						System.out.println("Impossible to read correctly the data type");
					}
					
					
					//extract all the Nffg in the nffgs
					Set<NodeReader> nodeSet = nffg_r.getNodes();
					
					
					
					//extract all the nodes in the single Nffg
//NODES
					for (NodeReader nr: nodeSet) {
						Node n = new Node();
						
						//store the name of the node
						n.setName(nr.getName());
						
						//extract the functionality of the node
						n.setFunctionality(Functionality
								.fromValue(nr.getFuncType().toString()));
						
						//add the node to the list of nodes
						nffg.getNode().add(n);
					
//LINKS - REMOVED BIDIRECIONAL LINKS
						Set<LinkReader> linkSet = nr.getLinks();
						for (LinkReader lr: linkSet){
								Link l = new Link();
								l.setName(lr.getName());
								l.setSrc(lr.getSourceNode().getName());
								l.setDst(lr.getDestinationNode().getName());
								nffg.getLink().add(l);
						}
					
					}
					nffgs.getNffg().add(nffg);
					
					
					
//POLICY
					Set<PolicyReader> setP = monitor.getPolicies();
					
					setP.stream().filter(p->
						{
							return p.getNffg().getName().equals(nffg.getName());
						})
					.forEach(p->{
						Policy policy = new Policy();
						policy.setName(p.getName());
						policy.setPositivity(p.isPositive());
						
						if(p.getResult()!=null){
							//result is available, write it on output
							Verification v = new Verification();
							
							v.setMessage(p.getResult().getVerificationResultMsg());
							v.setResult(p.getResult().getVerificationResult());
							v.setVerificationTime(convertCalendar(p.getResult().getVerificationTime()));
							
							policy.setVerification(v);
						}
						else{
							policy.setVerification(null);
						}
						
						
						
						ReachabilityPolicyReader reachability =(ReachabilityPolicyReader)p;
						policy.setSrc(reachability.getSourceNode().getName());
						policy.setDst(reachability.getDestinationNode().getName());
						
//TRAVERSAL POLICY						
						if((reachability instanceof TraversalPolicyReader) 
								//this additional check is used to avoid reachability policies without functionalities
								&& ((TraversalPolicyReader)reachability).getTraversedFuctionalTypes().size()!=0){
							
							Traversal t = new Traversal();
							
							Set<FunctionalType> traversalSetFunctionalities = ((TraversalPolicyReader)reachability).getTraversedFuctionalTypes();
							traversalSetFunctionalities.stream().forEach(f->{
								t.getFunctionality().add(Functionality.fromValue(f.toString()));
							});
							policy.setTraversal(t);
						}
//REACHABILITY						
//						else{
//						}
						nffg.getPolicy().add(policy);
						
						
						
						
					});	
					
					//Set<ReachabilityPolicyReader> setRP = monitor.getPolicies();
					
			}
			
			//SYSTEM.OUT PRINTING
			/*
			nffgs.getNffg()
				.forEach(a-> 
						{
							System.out.println(a.getName() + "\tUpdate: " +a.getLastUpdate().toString() + "\nNodes:");
							a.getNode().forEach(b->{
								//NODES
								System.out.println("\t" + b.getName() + "\t"+ b.getFunctionality().toString());
								
							});
							
							System.out.println("Links:");
							a.getLink().forEach(c->{
								//LINKS
								System.out.println("\t\tLink: "+ c.getName() +"\tSrc: "+c.getSrc() + "\tdst: " + c.getDst());
							});
							
							a.getPolicy().forEach(pol ->{
								//POLICY
								System.out.println("\tPolicy:\t" + pol.getName() + "\tPositivity: " + pol.isPositivity() + "\t");
								if(pol.getVerification()!=null){
									System.out.println("\t\t\tMessage:\t" + pol.getVerification().getMessage());
									System.out.println("\t\t\tVerif Time:\t" + pol.getVerification().getVerificationTime().toString());
									System.out.println("\t\t\tSatisfied:\t" + pol.getVerification().isResult());
									
								}
								
							});
						}
				
						);
			*/
			this.nffgs=nffgs;
	}

	/**
	 * @param args
	 * Describe the outpuf file where the xml will be created
	 * 
	 */
	public static void main(String[] args) {
		NffgInfoSerializer wf;
		
		//---TODO: verify if it is necessary.
		if(args.length<1){
			System.err.println("error in receiving the output file name");
			return;
		}
		
		//System.setProperty("it.polito.dp2.NFFG.sol1.NffgInfo.file", args[0]);
		//-------------------------------------
		
		
		try {
			wf = new NffgInfoSerializer();
			wf.prepareToMarshal();
			if(wf.nffgs!=null){
				wf.printXML(args[0]);
			}
			else{
				System.out.println("Impossible to create the XML. Something was wrong while converting data for the XML document\n");
			}
		} catch (NffgVerifierException e) {
			System.err.println("Could not instantiate data generator.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 
	 * @param filename
	 */
    public void printXML(String filename){
    	File file = null;
    	try{
    		file = new File(filename);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	JAXBContext jaxbContext;
		try {
		
			SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			jaxbContext = JAXBContext.newInstance(Nffgs.class);
			Marshaller jaxbMarshaller = null;
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//Validation of the schema - from JAXB-unmarshal-validate
			jaxbMarshaller.setSchema(sf.newSchema(new File("xsd/nffgInfo.xsd")));
			jaxbMarshaller.marshal(this.nffgs, file);
			
			System.out.println("XML file correctly written in " + filename);
		
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
    }

	
	/**
	 * Method used to convert a simple calendar to a XMLGregorianCalendar
	 * @param cal
	 * @return
	 */
	private XMLGregorianCalendar convertCalendar (Calendar cal){
		Date calendarDate = cal.getTime();
		
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(calendarDate);
//TODO ENABLE
		c.setTimeZone(cal.getTimeZone());
		XMLGregorianCalendar date2 = null;
		try {
			date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			System.out.println("Error in converting data type");
			e.printStackTrace();
		}
		return date2;
	}
}

