package net.ddns.minersonline.engine.plugins;

import java.security.*;

/**
 * Custom policy for the security-test.
 *
 */
public class PluginPolicy extends Policy {

	/**
	 * Returns {@link AllPermission} for any code sources that TODO:are allowed
	 * and an empty set of permissions for code sources that TODO:are not
	 * , denying access to all local resources to the rogue
	 * plugin.
	 * 
	 * @param codeSource
	 *            The code source to get the permission for
	 * @return The permissions for the given code source
	 */
	public PermissionCollection getPermissions(CodeSource codeSource) {
		Permissions p = new Permissions();
		p.add(new AllPermission());
		return p;
	}

	/**
	 * Does nothing.
	 */
	public void refresh() {
	}

}
