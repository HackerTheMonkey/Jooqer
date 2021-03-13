package io.codematters;

import java.util.List;

import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import io.codematters.docker.DockerCommands;
import io.codematters.docker.DockerConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerSpotifyRunner {

    /*Docker needs to be running on your machine*/
    public static void main(String[] args) {
        String image = "mysql:8.0.2";

        DockerConfig dockerConfig = new DockerConfig(image);
        DockerCommands docker = new DockerCommands(dockerConfig.getClient());

        final ContainerCreation container = docker.createContainer(dockerConfig.getContainerConfig());
        log.info("container: {}", container);

        final List<Container> containers = docker.listContainers();
        log.info("containers: {}", containers);

        docker.startContainer(container);
        log.info("container successfully started");

//        Enable for extra logging - this may stop the container from being killed currently
//        docker.enableContainerLogging(container);

        final ContainerInfo info = docker.inspectContainer(container);
        log.info("here is your container info: {}", info);

        /*Run liquibase & JOOQ logic here after container has been spun up*/

        docker.stopContainer(container);
        log.info("container stopped now");

        log.info("killing docker client now");
        docker.closeDockerClient();
    }
}
