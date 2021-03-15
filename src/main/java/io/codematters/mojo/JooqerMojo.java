package io.codematters.mojo;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Goal that greets this crazy world!
 */
@Mojo(name = "hello", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class JooqerMojo extends AbstractMojo {
    public void execute() throws MojoExecutionException {
        getLog().info("Yo world, sup?");
    }
}
