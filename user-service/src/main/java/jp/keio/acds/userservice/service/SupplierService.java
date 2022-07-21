package jp.keio.acds.userservice.service;

import com.scalar.db.exception.transaction.TransactionException;
import jp.keio.acds.userservice.dto.Supplier;
import jp.keio.acds.userservice.dto.SupplierCreate;
import jp.keio.acds.userservice.repository.SupplierRepository;

import java.util.UUID;

public class SupplierService extends BaseService {
    private static final SupplierRepository supplierRepo = new SupplierRepository();

    public Supplier get(UUID supplierId) {
        return this.execute(tx -> supplierRepo.get(tx, supplierId), null);
    }

    public Supplier[] list() {
        return this.execute(supplierRepo::list, null);
    }

    public Supplier create(SupplierCreate supplierIn) {
        return this.execute(tx -> {
            Supplier supplier = supplierRepo.create(tx, supplierIn);
            tx.commit();
            return supplier;
        }, null);
    }
}
