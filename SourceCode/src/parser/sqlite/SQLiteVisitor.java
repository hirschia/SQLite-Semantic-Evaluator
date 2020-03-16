// Generated from C:\Users\Piyush\Documents\NetBeansProjects\TestingGrammar\src\sqlitesyntax\grammar\SQLiteSyntax.g4 by ANTLR 4.2.2
package sqlitesyntax.grammar;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SQLiteSyntaxParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SQLiteSyntaxVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#insert_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_stmt(@NotNull SQLiteSyntaxParser.Insert_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#sql_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_stmt(@NotNull SQLiteSyntaxParser.Sql_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#table_or_index_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_or_index_name(@NotNull SQLiteSyntaxParser.Table_or_index_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#compound_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_operator(@NotNull SQLiteSyntaxParser.Compound_operatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#indexed_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexed_column(@NotNull SQLiteSyntaxParser.Indexed_columnContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#create_index_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_index_stmt(@NotNull SQLiteSyntaxParser.Create_index_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#rollback_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollback_stmt(@NotNull SQLiteSyntaxParser.Rollback_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name(@NotNull SQLiteSyntaxParser.Column_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#qualified_table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_table_name(@NotNull SQLiteSyntaxParser.Qualified_table_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#savepoint_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSavepoint_name(@NotNull SQLiteSyntaxParser.Savepoint_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#result_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult_column(@NotNull SQLiteSyntaxParser.Result_columnContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#error_message}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_message(@NotNull SQLiteSyntaxParser.Error_messageContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#select_core}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_core(@NotNull SQLiteSyntaxParser.Select_coreContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#create_trigger_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_trigger_stmt(@NotNull SQLiteSyntaxParser.Create_trigger_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name(@NotNull SQLiteSyntaxParser.Function_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#sql_stmt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_stmt_list(@NotNull SQLiteSyntaxParser.Sql_stmt_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table_stmt(@NotNull SQLiteSyntaxParser.Drop_table_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_name(@NotNull SQLiteSyntaxParser.Any_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#database_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_name(@NotNull SQLiteSyntaxParser.Database_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#module_argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_argument(@NotNull SQLiteSyntaxParser.Module_argumentContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#join_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_operator(@NotNull SQLiteSyntaxParser.Join_operatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#vacuum_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVacuum_stmt(@NotNull SQLiteSyntaxParser.Vacuum_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#factored_select_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactored_select_stmt(@NotNull SQLiteSyntaxParser.Factored_select_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#transaction_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_name(@NotNull SQLiteSyntaxParser.Transaction_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#cte_table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCte_table_name(@NotNull SQLiteSyntaxParser.Cte_table_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#unary_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_operator(@NotNull SQLiteSyntaxParser.Unary_operatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#common_table_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommon_table_expression(@NotNull SQLiteSyntaxParser.Common_table_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#drop_trigger_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_trigger_stmt(@NotNull SQLiteSyntaxParser.Drop_trigger_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#new_table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_table_name(@NotNull SQLiteSyntaxParser.New_table_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_name(@NotNull SQLiteSyntaxParser.Type_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#table_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_alias(@NotNull SQLiteSyntaxParser.Table_aliasContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#module_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_name(@NotNull SQLiteSyntaxParser.Module_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#commit_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommit_stmt(@NotNull SQLiteSyntaxParser.Commit_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#trigger_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger_name(@NotNull SQLiteSyntaxParser.Trigger_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#create_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_stmt(@NotNull SQLiteSyntaxParser.Create_table_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#pragma_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma_value(@NotNull SQLiteSyntaxParser.Pragma_valueContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#column_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_constraint(@NotNull SQLiteSyntaxParser.Column_constraintContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#savepoint_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSavepoint_stmt(@NotNull SQLiteSyntaxParser.Savepoint_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#detach_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDetach_stmt(@NotNull SQLiteSyntaxParser.Detach_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#column_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_def(@NotNull SQLiteSyntaxParser.Column_defContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#table_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_constraint(@NotNull SQLiteSyntaxParser.Table_constraintContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#column_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_alias(@NotNull SQLiteSyntaxParser.Column_aliasContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#delete_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_stmt(@NotNull SQLiteSyntaxParser.Delete_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#release_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelease_stmt(@NotNull SQLiteSyntaxParser.Release_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#foreign_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeign_table(@NotNull SQLiteSyntaxParser.Foreign_tableContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#create_view_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_view_stmt(@NotNull SQLiteSyntaxParser.Create_view_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(@NotNull SQLiteSyntaxParser.ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#signed_number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSigned_number(@NotNull SQLiteSyntaxParser.Signed_numberContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#delete_stmt_limited}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_stmt_limited(@NotNull SQLiteSyntaxParser.Delete_stmt_limitedContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#index_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_name(@NotNull SQLiteSyntaxParser.Index_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#select_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_stmt(@NotNull SQLiteSyntaxParser.Select_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#create_virtual_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_virtual_table_stmt(@NotNull SQLiteSyntaxParser.Create_virtual_table_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_stmt(@NotNull SQLiteSyntaxParser.Alter_table_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#attach_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttach_stmt(@NotNull SQLiteSyntaxParser.Attach_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#pragma_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma_stmt(@NotNull SQLiteSyntaxParser.Pragma_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(@NotNull SQLiteSyntaxParser.Table_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(@NotNull SQLiteSyntaxParser.NameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#join_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_constraint(@NotNull SQLiteSyntaxParser.Join_constraintContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#reindex_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReindex_stmt(@NotNull SQLiteSyntaxParser.Reindex_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(@NotNull SQLiteSyntaxParser.ParseContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#begin_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBegin_stmt(@NotNull SQLiteSyntaxParser.Begin_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#table_or_subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_or_subquery(@NotNull SQLiteSyntaxParser.Table_or_subqueryContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#error}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError(@NotNull SQLiteSyntaxParser.ErrorContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#ordering_term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrdering_term(@NotNull SQLiteSyntaxParser.Ordering_termContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(@NotNull SQLiteSyntaxParser.KeywordContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#with_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_clause(@NotNull SQLiteSyntaxParser.With_clauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#pragma_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma_name(@NotNull SQLiteSyntaxParser.Pragma_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#select_or_values}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_or_values(@NotNull SQLiteSyntaxParser.Select_or_valuesContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#simple_select_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_select_stmt(@NotNull SQLiteSyntaxParser.Simple_select_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#view_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitView_name(@NotNull SQLiteSyntaxParser.View_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#join_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_clause(@NotNull SQLiteSyntaxParser.Join_clauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeign_key_clause(@NotNull SQLiteSyntaxParser.Foreign_key_clauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#literal_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral_value(@NotNull SQLiteSyntaxParser.Literal_valueContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#drop_index_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_index_stmt(@NotNull SQLiteSyntaxParser.Drop_index_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#compound_select_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_select_stmt(@NotNull SQLiteSyntaxParser.Compound_select_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#update_stmt_limited}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_stmt_limited(@NotNull SQLiteSyntaxParser.Update_stmt_limitedContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#collation_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollation_name(@NotNull SQLiteSyntaxParser.Collation_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#update_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_stmt(@NotNull SQLiteSyntaxParser.Update_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#drop_view_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_view_stmt(@NotNull SQLiteSyntaxParser.Drop_view_stmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#raise_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaise_function(@NotNull SQLiteSyntaxParser.Raise_functionContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#conflict_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConflict_clause(@NotNull SQLiteSyntaxParser.Conflict_clauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLiteSyntaxParser#analyze_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyze_stmt(@NotNull SQLiteSyntaxParser.Analyze_stmtContext ctx);
}