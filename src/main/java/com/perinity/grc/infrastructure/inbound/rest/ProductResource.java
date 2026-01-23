package com.perinity.grc.infrastructure.inbound.rest;

import com.perinity.grc.application.domain.model.Product;
import com.perinity.grc.application.service.ProductService;
import com.perinity.grc.infrastructure.inbound.rest.dto.CreateProductRequest;
import com.perinity.grc.infrastructure.inbound.rest.dto.ProductResponse;
import com.perinity.grc.infrastructure.inbound.rest.dto.UnsoldProductDTO;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    private final ProductService service;

    @Inject
    public ProductResource(ProductService service) {
        this.service = service;
    }

    @POST
    public Response create(@Valid CreateProductRequest request) {
        Product domain = toDomain(request);
        Product created = service.create(domain);
        return Response.created(URI.create("/products/" + created.getId()))
                .entity(toResponse(created))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        return service.findById(id)
                .map(p -> Response.ok(toResponse(p)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public List<ProductResponse> listAll() {
        return service.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid CreateProductRequest request) {
        Product domain = toDomain(request);
        Product updated = service.update(id, domain);
        return Response.ok(toResponse(updated)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        if (service.delete(id)) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private Product toDomain(CreateProductRequest request) {
        Product p = new Product();
        p.setName(request.name());
        p.setType(request.type());
        p.setDetails(request.details());
        p.setDimensions(request.dimensions());
        p.setWeight(request.weight());
        p.setPurchasePrice(request.purchasePrice());
        p.setSalePrice(request.salePrice());
        return p;
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(), p.getName(), p.getType(), p.getDetails(),
                p.getDimensions(), p.getWeight(), p.getPurchasePrice(),
                p.getSalePrice(), p.getCreatedAt());
    }

    @GET
    @Path("/report/oldest")
    public List<UnsoldProductDTO> getOldestReport() {
        return service.getOldestProductsReport();
    }
}