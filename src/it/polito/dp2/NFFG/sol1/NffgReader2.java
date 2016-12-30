package it.polito.dp2.NFFG.sol1;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.sol1.jaxb.Link;
import it.polito.dp2.NFFG.sol1.jaxb.Nffg;
import it.polito.dp2.NFFG.sol1.jaxb.Node;

public class NffgReader2 extends NamedEntityReader2 implements NffgReader {

	private Calendar updateTime = null;
	private Set<NodeReader> setNodesReader2 = null;
	
	public NffgReader2(Nffg nffg){
		
		super(nffg.getName());
		
		updateTime = (nffg.getLastUpdate().toGregorianCalendar());
		
		List<Node> listn = nffg.getNode();
		
		//create the NodeReader2 for each node and add to the set
		
		
		
		setNodesReader2 = new LinkedHashSet<NodeReader>();
		for(Node n : listn){
			NodeReader nr = new NodeReader2(n);
			setNodesReader2.add(nr);
		}
		
		//for each node create the correspondant link
		//filter the links having src or dst equal to the current node and save it to a list
		//extract the correct node reader element and add the available links

		
		
		
		for(Node n : listn){
			List<Link> lll = 
					nffg.getLink().stream().filter( l ->{
//FIX: Added only the link in which the source node is the correct node, or it corresponds to the destination and it is bidirectional 
//						return(l.getSrc().equals(n.getName())  ||  ( l.getDst().equals(n.getName()) && l.isBidirectional()) ); 
						return(l.getSrc().equals(n.getName()) ); 
					}).collect(Collectors.toList());
			
			
			for(NodeReader nr : setNodesReader2){
				if(nr.getName().equals(n.getName())){
					((NodeReader2) nr).addLinks(setNodesReader2,lll,n);
					
				}
			}
		}
	}
	

	@Override
	public NodeReader getNode(String arg0) {
		for(NodeReader nr2 : setNodesReader2){
			if(nr2.getName().equals(arg0)){
				return nr2;
			}
		}
		return null;
	}

	@Override
	public Set<NodeReader> getNodes() {
		return setNodesReader2;
	}
	

	@Override
	public Calendar getUpdateTime() {
		return updateTime;
	}

}
