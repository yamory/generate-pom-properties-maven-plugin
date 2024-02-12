package io.yamory;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;


public class GeneratePomPropertiesTest {
    @Rule
    public MojoRule mojoRule = new MojoRule();

    @Rule
    public TestResources resources = new TestResources("src/test/resources", "target/test-classes");

    @Test
    public void testMojoHasHelpGoal() throws Exception {
        File baseDir = resources.getBasedir("basic");
        File pom = new File(baseDir, "pom.xml");

        Mojo mojo = mojoRule.lookupMojo("help", pom);
        mojo.execute();
    }

    @Test
    public void testGoalSucceeds() throws Exception {
        File baseDir = resources.getBasedir("basic");
        File pom = new File(baseDir, "pom.xml");

        Mojo plugin = mojoRule.lookupMojo("help", pom);
        assertNotNull(plugin);
        plugin.execute();
    }
}
