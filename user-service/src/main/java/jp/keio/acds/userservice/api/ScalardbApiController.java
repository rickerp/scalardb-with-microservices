package jp.keio.acds.userservice.api;

import java.util.UUID;


import io.swagger.v3.oas.annotations.Parameter;
import jp.keio.acds.userservice.service.BaseService;
import jp.keio.acds.userservice.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-07-21T23:35:10.678+09:00[Asia/Tokyo]")
@Controller
@RequestMapping("${openapi.userService.base-path:}")
public class ScalardbApiController implements ScalardbApi {

    private static final BaseService baseService = new BaseService();
    private final NativeWebRequest request;

    @Autowired
    public ScalardbApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> join(UUID transactionId) {
        baseService.joinTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> prepare(UUID transactionId) {
        baseService.prepareTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> commit(UUID transactionId) {
        baseService.commitTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> rollback(UUID transactionId) {
        baseService.rollbackTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
