package wicket.kronos;

import wicket.authorization.strategies.role.IRoleCheckingStrategy;
import wicket.authorization.strategies.role.Roles;

/**
 * @author postma
 */
public class RoleCheckingStrategy implements IRoleCheckingStrategy {

	public boolean hasAnyRole(Roles userroles)
	{
		Roles rolesset = DataProcessor.getRoles();
		return rolesset.hasAnyRole(userroles);
	}
}
