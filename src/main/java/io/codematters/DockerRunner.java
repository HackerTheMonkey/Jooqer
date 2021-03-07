package io.codematters;

import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import io.codematters.docker.DockerCmds;

public class DockerRunner {

    public static void main(String[] args) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://localhost:2375")
                .withDockerTlsVerify(false)
                .withDockerCertPath("/home/user/.docker")
//                .withRegistryUsername(registryUser)
//                .withRegistryPassword(registryPass)
//                .withRegistryEmail(registryMail)
//                .withRegistryUrl(registryUrl)
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
//                .sslConfig(config.getSSLConfig())
                .build();

        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        DockerCmds docker = new DockerCmds(dockerClient);

        CreateContainerResponse container = docker.createContainer();

        docker.startContainer(container);

        InspectContainerResponse inspectContainerResponse = docker.inspectContainer(container);

        List<Image> images = docker.listImages();

        String imageId = docker.buildImage();

        InspectImageResponse inspectImageResponse = docker.inspectImage(imageId);

        docker.removeImage(imageId);

        docker.stopContainer(container);

        docker.killContainer(container);


    }


}
