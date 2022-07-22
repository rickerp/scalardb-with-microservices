package jp.keio.acds.userservice.service;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.TwoPhaseCommitTransactionManager;
import jp.keio.acds.userservice.dto.Supplier;
import jp.keio.acds.userservice.dto.SupplierCreate;
import jp.keio.acds.userservice.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SupplierService extends BaseService {
    private final SupplierRepository supplierRepo;


    @Autowired
    public SupplierService(SupplierRepository supplierRepo,
                        DistributedTransactionManager manager,
                        TwoPhaseCommitTransactionManager microserviceManager) {
        super(manager, microserviceManager);
        this.supplierRepo = supplierRepo;
    }

    public Supplier get(UUID supplierId) {
        return this.execute((Transaction<Supplier>) tx -> supplierRepo.get(tx, supplierId), null);
    }

    public Supplier[] list() {
        return this.execute(supplierRepo::list, null);
    }

    public Supplier create(SupplierCreate supplierIn) {
        return this.execute((Transaction<Supplier>) tx -> {
            Supplier supplier = supplierRepo.create(tx, supplierIn);
            tx.commit();
            return supplier;
        }, null);
    }
}
