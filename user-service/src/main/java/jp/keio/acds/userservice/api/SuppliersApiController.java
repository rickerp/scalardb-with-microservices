package jp.keio.acds.userservice.api;

import com.scalar.db.exception.transaction.TransactionException;
import jp.keio.acds.userservice.dto.Supplier;
import jp.keio.acds.userservice.dto.SupplierCreate;
import jp.keio.acds.userservice.service.SupplierService;
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
import java.util.UUID;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-07-21T11:23:14.446+09:00[Asia/Tokyo]")
@Controller
@RequestMapping("${openapi.userService.base-path:}")
public class SuppliersApiController implements SuppliersApi {
    private static final SupplierService supplierService = new SupplierService();


    private final NativeWebRequest request;

    @Autowired
    public SuppliersApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Supplier> getSupplier(UUID supplierId) {
        try {
            return new ResponseEntity(supplierService.get(supplierId), HttpStatus.OK);
        } catch (TransactionException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Supplier>> listSuppliers() {
        try {
            return new ResponseEntity(Arrays.asList(supplierService.list()), HttpStatus.OK);
        } catch (TransactionException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Supplier> createSupplier(SupplierCreate body) {
        try {
            return new ResponseEntity(supplierService.create(body), HttpStatus.OK);
        } catch (TransactionException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
