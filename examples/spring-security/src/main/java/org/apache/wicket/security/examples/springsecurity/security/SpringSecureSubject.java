package org.apache.wicket.security.examples.springsecurity.security;

import java.util.Iterator;

import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.security.hive.authorization.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

/**
 * Subject that gets is principals from the authenticated user in the
 * {@link org.springframework.security.context.SecurityContextHolder}. This class is
 * converts all authorities to {@link SpringSecurePrincipal}s but could serve as a
 * template for your implementation. When Spring Security is configured with a UserDetails
 * service, the subject will contain the UserDetails object for later reference.
 * 
 * @author marrink
 * @author Olger Warnier
 */
public class SpringSecureSubject extends DefaultSubject
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(SpringSecureSubject.class);

	private UserDetails userDetails = null;

	/**
	 * Constructor.
	 */
	public SpringSecureSubject()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		GrantedAuthority[] authorities = authentication.getAuthorities();
		if (authorities != null)
		{
			Principal principal;
			for (int i = 0; i < authorities.length; i++)
			{
				principal = convert(authorities[i]);
				if (principal != null)
					addPrincipal(principal);
			}
		}
		if (authentication.getPrincipal() instanceof UserDetails)
		{
			userDetails = (UserDetails) authentication.getPrincipal();
		}
		else if (authentication.getDetails() instanceof UserDetails)
		{
			userDetails = (UserDetails) authentication.getDetails();
		}
	}

	/**
	 * Converts a {@link org.springframework.security.GrantedAuthority} to a
	 * {@link Principal}
	 * 
	 * @param authority
	 * @return principal or null if the authority could not be converted
	 */
	protected Principal convert(GrantedAuthority authority)
	{
		if (log.isDebugEnabled())
		{
			log.debug("convert:" + authority.toString());
		}

		return new SpringSecurePrincipal(authority.getAuthority());
	}

	/* show the full login name and principals for the login */
	@Override
	public String toString()
	{
		StringBuilder strBld = new StringBuilder("SpringSecureSubject for ");
		strBld.append(SecurityContextHolder.getContext().getAuthentication().getCredentials()
			.toString());
		strBld.append(" contains: ");
		Iterator<Principal> iter = getPrincipals().iterator();
		while (iter.hasNext())
		{
			SpringSecurePrincipal prin = (SpringSecurePrincipal) iter.next();
			strBld.append(prin.getName());
			strBld.append(" ");
		}
		return strBld.toString();
	}

	public UserDetails getUserDetails()
	{
		return userDetails;
	}

}
