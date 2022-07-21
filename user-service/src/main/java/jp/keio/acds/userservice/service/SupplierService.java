package jp.keio.acds.userservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.exception.transaction.TransactionException;
import jp.keio.acds.userservice.dto.Supplier;
import jp.keio.acds.userservice.dto.SupplierCreate;
import jp.keio.acds.userservice.repository.SupplierRepository;

import java.util.UUID;

public class SupplierService extends BaseService {
    private static final SupplierRepository supplierRepo = new SupplierRepository();

    public Supplier get(UUID supplierId) throws TransactionException {
        DistributedTransaction tx = startTransaction();
        Supplier supplier = supplierRepo.get(tx, supplierId);
        tx.abort();
        return supplier;
    }

    public Supplier[] list() throws TransactionException {
        DistributedTransaction tx = startTransaction();
        Supplier[] suppliers = supplierRepo.list(tx);
        tx.abort();
        return suppliers;
    }

    public Supplier create(SupplierCreate supplierIn) throws TransactionException {
        DistributedTransaction tx = startTransaction();
        Supplier supplier = supplierRepo.create(tx, supplierIn);
        tx.commit();
        return supplier;
    }
}
