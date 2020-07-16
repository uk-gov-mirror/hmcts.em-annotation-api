package uk.gov.hmcts.reform.em.annotation.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.em.annotation.rest.util.HeaderUtil;
import uk.gov.hmcts.reform.em.annotation.service.MetadataService;
import uk.gov.hmcts.reform.em.annotation.service.dto.MetadataDto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * REST controller for managing Metadata.
 */
@RestController
@RequestMapping("/api/metadata")
@RequiredArgsConstructor
public class MetaDataResource {

    private final Logger log = LoggerFactory.getLogger(MetaDataResource.class);

    private static final String ENTITY_NAME = "metadata";

    private final MetadataService metadataService;

    /**
     * POST  /metadata : Create a new metadata.
     *
     * @param metadataDto the metadataDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metadataDto
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Create an metadataDto", notes = "A POST request to create an metadataDto")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created", response = MetadataDto.class),
        @ApiResponse(code = 400, message = "metadataDto not valid"),
        @ApiResponse(code = 401, message = "Unauthorised"),
        @ApiResponse(code = 403, message = "Forbidden"),
    })
    @PostMapping("/")
    public ResponseEntity<MetadataDto> createMetaData(@Validated @RequestBody MetadataDto metadataDto) throws URISyntaxException {

        log.debug("REST request to save Metadata : {}", metadataDto);

        MetadataDto createdMetadataDto = metadataService.save(metadataDto);

        final URI uri = new URI("/api/metadata/" + createdMetadataDto.getDocumentId());

        return ResponseEntity.created(uri)
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, createdMetadataDto.getDocumentId().toString()))
                .body(createdMetadataDto);
    }

    /**
     * GET  /metadata/:documentId : get the metadata for a specific document.
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "Get the metadata for Document ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MetadataDto.class),
        @ApiResponse(code = 401, message = "Unauthorised"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping("/{documentId}")
    public ResponseEntity<MetadataDto> getMetadata(@PathVariable UUID documentId) {

        log.debug("REST request to get the metadata for the Document");
        MetadataDto metadataDto = metadataService.findByDocumentId(documentId);

        return ResponseEntity.ok().body(metadataDto);
    }

}