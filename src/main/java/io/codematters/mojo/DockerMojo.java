package io.codematters.mojo;

import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import io.codematters.docker.DockerCommands;
import io.codematters.docker.DockerConfig;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

/**
* Must run mvn install to recompile & install the latest version of this code to your machine.
 *
 * Example terminal command to run this goal with custom database image:
 * mvn io.codematters:jooqer-maven-plugin:1.0.0-SNAPSHOT:docker "-Ddocker.image=postgres:13.2"
* */
@Mojo(name = "docker", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class DockerMojo extends AbstractMojo {

    private final String DEFAULT_IMAGE = "mysql:8.0.2";

    @Parameter(property = "docker.image", defaultValue = DEFAULT_IMAGE)
    private String image;

    public void execute() throws MojoExecutionException {
        getLog().info("Building docker image: " + image);

        if (DEFAULT_IMAGE.equals(image)) {
             getLog().info("You can override this by calling this goal with -Ddocker.image=${DOCKER_IMAGE}");
        }

        DockerConfig dockerConfig = new DockerConfig(image);
        DockerCommands docker = new DockerCommands(dockerConfig.getClient());

        final ContainerCreation container = docker.createContainer(dockerConfig.getContainerConfig());
        getLog().info("Container: " + container);

        final List<Container> containers = docker.listContainers();
        getLog().info("Containers: " + containers);

        docker.startContainer(container);
        getLog().info("Container successfully started");

//        Enable for extra logging - this may stop the container from being killed currently
//        docker.enableContainerLogging(container);

        final ContainerInfo info = docker.inspectContainer(container);
        getLog().info("Container info: " + info);

        /*Run liquibase & JOOQ logic here after container has been spun up*/

        docker.stopContainer(container);
        getLog().info("Stopping container...");

        docker.removeContainer(container);
        getLog().info("Removing container...");

        docker.closeDockerClient();
        getLog().info("Killing docker client...");
    }
}
