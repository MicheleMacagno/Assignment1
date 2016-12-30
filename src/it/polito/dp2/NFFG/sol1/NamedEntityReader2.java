package it.polito.dp2.NFFG.sol1;

import it.polito.dp2.NFFG.NamedEntityReader;

public class NamedEntityReader2 implements NamedEntityReader {

	private String name=null;
		
	public NamedEntityReader2(String name){
		this.name=name;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
