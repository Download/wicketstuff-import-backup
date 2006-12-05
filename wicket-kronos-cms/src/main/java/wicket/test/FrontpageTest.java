package wicket.test;

import junit.framework.TestCase;
import wicket.PageParameters;
import wicket.kronos.KronosSession;
import wicket.kronos.frontpage.Frontpage;

/**
 * @author postma
 */
public class FrontpageTest extends TestCase {

	PageParameters pageparams = null;

	Frontpage frontpage = null;

	Frontpage nullpointertest = null;

	/**
	 * @param name
	 */
	public FrontpageTest(String name)
	{
		super(name);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		pageparams = new PageParameters();
		frontpage = new Frontpage(pageparams);
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		pageparams = null;
		frontpage = null;
	}

	/**
	 * Test the Frontpage constructor for not accepting null
	 */
	public void testFrontpage()
	{
		System.out.println("Hij dut");
		try
		{
			nullpointertest = new Frontpage(null);
			fail("Should raise an IndexOutOfBoundsException");
		}
		catch (IndexOutOfBoundsException success)
		{
		}
	}

	/**
	 * Test retreiving the KronosSession object
	 */
	void testGetKronosSession()
	{
		assertTrue(
				"Frontpage.getKronosSession() isn't an instance of KronosSession",
				frontpage.getKronosSession() instanceof KronosSession);
		assertNotNull("Frontpage.getKronosSession() can't be 'null'", frontpage
				.getKronosSession());
	}

}
