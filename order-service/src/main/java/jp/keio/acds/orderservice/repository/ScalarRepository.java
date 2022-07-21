package jp.keio.acds.orderservice.repository;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Get;
import com.scalar.db.api.Result;
import com.scalar.db.api.Scan;
import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.io.Key;
import jp.keio.acds.orderservice.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ScalarRepository<T> {

    public T get(DistributedTransaction tx, String id) throws CrudException, NotFoundException {
        return toDto(tx.get(getQuery(id)).orElseThrow(() -> new NotFoundException(id)));
    }

    public List<T> scan(DistributedTransaction tx) throws CrudException {
        return tx.scan(scanQuery()).stream().map(this::toDto).collect(Collectors.toList());
    }

    public boolean exists(DistributedTransaction tx, String id) throws CrudException {
        return tx.get(getQuery(id)).isPresent();
    }

    private Get getQuery(String id) {
        return Get.newBuilder()
                .namespace(getNamespace())
                .table(getTable())
                .partitionKey(Key.ofText(getPartitionKey(), id))
                .build();
    }

    private Scan scanQuery() {
        return Scan.newBuilder()
                .namespace(getNamespace())
                .table(getTable())
                .all()
                .build();
    }

    protected abstract String getNamespace();
    protected abstract String getTable();
    protected abstract String getPartitionKey();

    protected abstract T toDto(Result result);
}
