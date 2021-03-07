package io.codematters;


import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class JooqerMojoTest {

    @Rule
    public MojoRule mojoRule = new MojoRule();

    @Rule
    public SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Before
    public void setup() {
        systemOutRule.clearLog();
    }

    @Test
    public void shouldSaySomething() throws Exception {
        // Given
        File pomFileThatUsesJooqer = new File("src/test/resources/project-to-test/pom.xml");

        // When
        executeMojo("hello", pomFileThatUsesJooqer);

        // Then
        assertThat(systemOutRule.getLog()).contains("[INFO] Yo world, sup?");
    }

    private void executeMojo(String goal, File clientPomFile) throws Exception {
        JooqerMojo mojo = (JooqerMojo) mojoRule.lookupMojo(goal, clientPomFile);
        mojo.execute();
    }

    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn() {
        assertTrue(true);
    }

}

