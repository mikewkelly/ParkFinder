package com.cpsc310.team_name.parkfinder.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface Resources extends ClientBundle {
	
	public static final Resources INSTANCE = GWT.create(Resources.class);
	
	public interface Style extends CssResource {
		
	}
	
	@ClientBundle.Source("ClientParkFinder.css")
	@CssResource.NotStrict 		// If a class is present in the css file,
	Style ClientParkFinder();   //but not in the interface, don't
	                         	// return any compiler errors
}