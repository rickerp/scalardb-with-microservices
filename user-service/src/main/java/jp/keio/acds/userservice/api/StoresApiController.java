package jp.keio.acds.userservice.api;

import jp.keio.acds.userservice.dto.Store;
import jp.keio.acds.userservice.dto.StoreCreate;
import jp.keio.acds.userservice.dto.StoreUpdate;
import jp.keio.acds.userservice.dto.TransactionUpdate;
import jp.keio.acds.userservice.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-07-21T00:21:06.629+09:00[Asia/Tokyo]")
@Controller
@RequestMapping("${openapi.userService.base-path:}")
public class StoresApiController implements StoresApi {
    private static final StoreService storeService = new StoreService();


    private final NativeWebRequest request;

    @Autowired
    public StoresApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Store> getStore(UUID storeId) {
        return new ResponseEntity(storeService.get(storeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Store>> listStores() {
        return new ResponseEntity(Arrays.asList(storeService.listStores()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Store> createStore(StoreCreate body) {
        return new ResponseEntity(storeService.createStore(body), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Store> updateStore(UUID storeId, StoreUpdate storeUpdate) {
        return new ResponseEntity(storeService.updateStore(storeId, storeUpdate), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteStore(UUID storeId) {
        storeService.deleteStore(storeId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> registerOrder(String storeId, TransactionUpdate transactionUpdate) {
        transactionUpdate.getTransactionId();
        return new ResponseEntity(HttpStatus.OK);
    }
}
