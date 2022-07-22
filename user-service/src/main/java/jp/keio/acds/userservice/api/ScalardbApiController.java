package jp.keio.acds.userservice.api;

import java.util.UUID;


import jp.keio.acds.userservice.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-07-21T23:35:10.678+09:00[Asia/Tokyo]")
@Controller
@RequestMapping("${openapi.userService.base-path:}")
public class ScalardbApiController implements ScalardbApi {

    private final BaseService baseService;
    private final NativeWebRequest request;

    @Autowired
    public ScalardbApiController(NativeWebRequest request, BaseService baseService) {
        this.request = request;
        this.baseService = baseService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> join(UUID transactionId) {
        System.out.println("JOIN");
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
