package com.kevinj.portfolio.issuetrack.storage.adapter.in;

import com.kevinj.portfolio.issuetrack.storage.application.port.in.StorageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class StorageController {

    private final StorageUseCase storageUseCase;

    @Operation(
        summary = "File upload",
        description = "Single file upload & return file-id"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "no file or invalid file"),
        @ApiResponse(responseCode = "500", description = "internal server error"),
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long upload(
        @RequestPart("file") MultipartFile file
    ) {
        return storageUseCase.upload(file);
    }

    @Operation(
        summary = "File delete",
        description = "Single file delete"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "invalid file path"),
        @ApiResponse(responseCode = "500", description = "internal server error"),
    })
    @DeleteMapping("/{fileId}")
    public void delete(
        @PathVariable("fileId") Long fileId
    ) {
        storageUseCase.delete(fileId);
    }
}
