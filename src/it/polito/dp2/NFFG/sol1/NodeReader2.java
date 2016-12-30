package it.polito.dp2.NFFG.sol1;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.sol1.jaxb.Link;
import it.polito.dp2.NFFG.sol1.jaxb.Node;

public class NodeReader2 extends NamedEntityReader2 implements NodeReader {

	
	private FunctionalType funcType=null;
	private Set<LinkReader> links=null;
	
	public NodeReader2(Node node){
		//node NAME
		super(node.getName());
		
		//node FUNCTIONALITY
		funcType = FunctionalType.fromValue(node.getFunctionality().name());
		
		//node Links -> moved in addLinks called by NffgReader2
		links = new LinkedHashSet<LinkReader>();
	}
	

	@Override
	public FunctionalType getFuncType() {
		return funcType;
	}

	@Override
	public Set<LinkReader> getLinks() {
		return links;
	}
	
	/**
	 * This method receives the Nffg element and the set of NodeReader. This way, it adds
	 * all the available links to the solution
	 * @param snr
	 */
	protected void addLinks(Set<NodeReader> snr,List<Link> lll,Node n){
		//for each link
		lll.stream().forEach(l->{
			
			//find source NodeReader
			List<NodeReader> nodoSrc =
					snr.stream().filter(nodeReader ->{
						return(
									nodeReader.getName().equals(l.getSrc())
									);
					}).collect(Collectors.toList());
					
			//find destination NodeReader
			List<NodeReader> nodoDst=
					snr.stream().filter(nodeReader ->{
						return(
									nodeReader.getName().equals(l.getDst())
									);
					}).collect(Collectors.toList());
			
			
			//create the new LinkReader and add to solution
			LinkReader2 lr2 = new LinkReader2(l.getName(),nodoSrc.get(0),nodoDst.get(0));
			
			//add only the links in which the name is correspon
			if(nodoSrc.get(0).getName().equals(n.getName())){
				
				
				Boolean toAdd=true;
				for(LinkReader lr : links){
					//if already exist a link with same name, src and destination discard it!
					if(
							lr.getName().equals(lr2.getName()) 
							&& 	lr.getSourceNode().getName().equals(	lr2.getSourceNode().getName()	) &&
								lr.getDestinationNode().getName().equals(	lr2.getDestinationNode().getName())	){
						toAdd=false;
						break;
					}
				}
				
				if(toAdd){
					links.add(lr2);
				}
			}

		});
	}
	

}
