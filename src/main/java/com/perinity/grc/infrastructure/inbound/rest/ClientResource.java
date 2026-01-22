package com.perinity.grc.infrastructure.inbound.rest;

import com.perinity.grc.application.domain.model.Client;
import com.perinity.grc.application.service.ClientService;
import com.perinity.grc.infrastructure.inbound.rest.dto.ClientResponse;
import com.perinity.grc.infrastructure.inbound.rest.dto.CreateClientRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

    private final ClientService service;

    @Inject
    public ClientResource(ClientService service) {
        this.service = service;
    }

    @POST
    public Response createClient(@Valid CreateClientRequest request) {
        // 1. Adapter: Convert DTO -> Domain
        Client domainClient = new Client();
        domainClient.setFullName(request.fullName());
        domainClient.setMotherName(request.motherName());
        domainClient.setAddress(request.address());
        domainClient.setCpf(request.cpf());
        domainClient.setRg(request.rg());
        domainClient.setBirthDate(request.birthDate());
        domainClient.setPhoneNumber(request.phoneNumber());
        domainClient.setEmail(request.email());

        Client savedClient = service.create(domainClient);

        ClientResponse responseDTO = new ClientResponse(
            savedClient.getId(),
            savedClient.getFullName(),
            savedClient.getMotherName(),
            savedClient.getAddress(),
            savedClient.getCpf(),
            savedClient.getRg(),
            savedClient.getBirthDate(),
            savedClient.getPhoneNumber(),
            savedClient.getCreatedAt(),
            savedClient.getEmail()
        );

        return Response.created(URI.create("/clients/" + savedClient.getId()))
                       .entity(responseDTO)
                       .build();
    }

    @GET
    public List<ClientResponse> listAll() {
        return service.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        return service.findById(id)
                .map(client -> Response.ok(toResponse(client)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    // Método auxiliar para não repetir a criação do DTO (Reutilize no POST também!)
    private ClientResponse toResponse(Client client) {
        return new ClientResponse(
            client.getId(),
            client.getFullName(),
            client.getMotherName(),
            client.getAddress(),
            client.getCpf(),
            client.getRg(),
            client.getBirthDate(),
            client.getPhoneNumber(),
            client.getCreatedAt(),
            client.getEmail()
        );
    }
}