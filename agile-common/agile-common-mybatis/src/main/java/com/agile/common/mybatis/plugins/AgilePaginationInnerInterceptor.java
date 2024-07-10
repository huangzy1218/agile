package com.agile.common.mybatis.plugins;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

/**
 * Reconstruct the paging plug-in.
 * When size is less than 0, set it directly to 0 to prevent incorrect query of the entire table.
 *
 * @author Huang Z.Y.
 */
@Data
@NoArgsConstructor
public class AgilePaginationInnerInterceptor extends PaginationInnerInterceptor {

    /**
     * Database type.
     *
     * @see #findIDialect(Executor)
     */
    private DbType dbType;

    /**
     * Dialect implementation class.
     *
     * @see #findIDialect(Executor)
     */
    private IDialect dialect;

    public AgilePaginationInnerInterceptor(DbType dbType) {
        this.dbType = dbType;
    }

    public AgilePaginationInnerInterceptor(IDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                            ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
        // If size is less than 0, set it directly to 0, that is, no data will be queried
        if (null != page && page.getSize() < 0) {
            page.setSize(0);
        }
        super.beforeQuery(executor, ms, page, rowBounds, resultHandler, boundSql);
    }

}
