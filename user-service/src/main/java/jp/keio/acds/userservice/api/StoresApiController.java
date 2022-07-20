package jp.keio.acds.userservice.api;

import com.scalar.db.exception.transaction.TransactionException;
import jp.keio.acds.userservice.dto.Store;
import jp.keio.acds.userservice.repository.StoreRepository;
import jp.keio.acds.userservice.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-07-15T04:00:53.397+09:00[Asia/Tokyo]")
@Controller
@RequestMapping("${openapi.userService.base-path:}")
public class StoresApiController implements StoresApi {
    private static final StoreService storeService = new StoreService();
    private final NativeWebRequest request;

    @Autowired
    public StoresApiController(NativeWebRequest request) {
        super();
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<List<Store>> listStores() {
        try {
            return new ResponseEntity(Arrays.asList(storeService.list()), HttpStatus.OK);
        } catch (TransactionException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
