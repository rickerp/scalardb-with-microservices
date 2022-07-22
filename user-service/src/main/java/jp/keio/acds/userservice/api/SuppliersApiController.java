package jp.keio.acds.userservice.api;

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
    private final SupplierService supplierService;


    private final NativeWebRequest request;

    @Autowired
    public SuppliersApiController(NativeWebRequest request, SupplierService supplierService) {
        this.request = request;
        this.supplierService = supplierService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Supplier> getSupplier(UUID supplierId) {
        return new ResponseEntity(supplierService.get(supplierId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Supplier>> listSuppliers() {
        return new ResponseEntity(Arrays.asList(supplierService.list()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Supplier> createSupplier(SupplierCreate body) {
        return new ResponseEntity(supplierService.create(body), HttpStatus.OK);
    }
}
