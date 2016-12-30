package it.polito.dp2.NFFG.sol1;

import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;

public class LinkReader2 extends NamedEntityReader2 implements LinkReader {

	private NodeReader dstNode=null;
	private NodeReader srcNode=null;
	
	public LinkReader2(String name,NodeReader src,NodeReader dst){
		super(name);
		this.srcNode=src;
		this.dstNode=dst;
	}
	
	
	@Override
	public NodeReader getDestinationNode() {
		return dstNode;
	}

	@Override
	public NodeReader getSourceNode() {
		return srcNode;
	}

}
