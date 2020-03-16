// Generated from C:\Users\Piyush\Documents\NetBeansProjects\TestingGrammar\src\sqlitesyntax\grammar\SQLiteSyntax.g4 by ANTLR 4.2.2
package sqlitesyntax.grammar;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SQLiteSyntaxParser}.
 */
public interface SQLiteSyntaxListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInsert_stmt(@NotNull SQLiteSyntaxParser.Insert_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInsert_stmt(@NotNull SQLiteSyntaxParser.Insert_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSql_stmt(@NotNull SQLiteSyntaxParser.Sql_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSql_stmt(@NotNull SQLiteSyntaxParser.Sql_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#table_or_index_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_or_index_name(@NotNull SQLiteSyntaxParser.Table_or_index_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#table_or_index_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_or_index_name(@NotNull SQLiteSyntaxParser.Table_or_index_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#compound_operator}.
	 * @param ctx the parse tree
	 */
	void enterCompound_operator(@NotNull SQLiteSyntaxParser.Compound_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#compound_operator}.
	 * @param ctx the parse tree
	 */
	void exitCompound_operator(@NotNull SQLiteSyntaxParser.Compound_operatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#indexed_column}.
	 * @param ctx the parse tree
	 */
	void enterIndexed_column(@NotNull SQLiteSyntaxParser.Indexed_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#indexed_column}.
	 * @param ctx the parse tree
	 */
	void exitIndexed_column(@NotNull SQLiteSyntaxParser.Indexed_columnContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#create_index_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_index_stmt(@NotNull SQLiteSyntaxParser.Create_index_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#create_index_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_index_stmt(@NotNull SQLiteSyntaxParser.Create_index_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#rollback_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRollback_stmt(@NotNull SQLiteSyntaxParser.Rollback_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#rollback_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRollback_stmt(@NotNull SQLiteSyntaxParser.Rollback_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#column_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name(@NotNull SQLiteSyntaxParser.Column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#column_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name(@NotNull SQLiteSyntaxParser.Column_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#qualified_table_name}.
	 * @param ctx the parse tree
	 */
	void enterQualified_table_name(@NotNull SQLiteSyntaxParser.Qualified_table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#qualified_table_name}.
	 * @param ctx the parse tree
	 */
	void exitQualified_table_name(@NotNull SQLiteSyntaxParser.Qualified_table_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#savepoint_name}.
	 * @param ctx the parse tree
	 */
	void enterSavepoint_name(@NotNull SQLiteSyntaxParser.Savepoint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#savepoint_name}.
	 * @param ctx the parse tree
	 */
	void exitSavepoint_name(@NotNull SQLiteSyntaxParser.Savepoint_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#result_column}.
	 * @param ctx the parse tree
	 */
	void enterResult_column(@NotNull SQLiteSyntaxParser.Result_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#result_column}.
	 * @param ctx the parse tree
	 */
	void exitResult_column(@NotNull SQLiteSyntaxParser.Result_columnContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#error_message}.
	 * @param ctx the parse tree
	 */
	void enterError_message(@NotNull SQLiteSyntaxParser.Error_messageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#error_message}.
	 * @param ctx the parse tree
	 */
	void exitError_message(@NotNull SQLiteSyntaxParser.Error_messageContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#select_core}.
	 * @param ctx the parse tree
	 */
	void enterSelect_core(@NotNull SQLiteSyntaxParser.Select_coreContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#select_core}.
	 * @param ctx the parse tree
	 */
	void exitSelect_core(@NotNull SQLiteSyntaxParser.Select_coreContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#create_trigger_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_trigger_stmt(@NotNull SQLiteSyntaxParser.Create_trigger_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#create_trigger_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_trigger_stmt(@NotNull SQLiteSyntaxParser.Create_trigger_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#function_name}.
	 * @param ctx the parse tree
	 */
	void enterFunction_name(@NotNull SQLiteSyntaxParser.Function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#function_name}.
	 * @param ctx the parse tree
	 */
	void exitFunction_name(@NotNull SQLiteSyntaxParser.Function_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#sql_stmt_list}.
	 * @param ctx the parse tree
	 */
	void enterSql_stmt_list(@NotNull SQLiteSyntaxParser.Sql_stmt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#sql_stmt_list}.
	 * @param ctx the parse tree
	 */
	void exitSql_stmt_list(@NotNull SQLiteSyntaxParser.Sql_stmt_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table_stmt(@NotNull SQLiteSyntaxParser.Drop_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table_stmt(@NotNull SQLiteSyntaxParser.Drop_table_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#any_name}.
	 * @param ctx the parse tree
	 */
	void enterAny_name(@NotNull SQLiteSyntaxParser.Any_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#any_name}.
	 * @param ctx the parse tree
	 */
	void exitAny_name(@NotNull SQLiteSyntaxParser.Any_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#database_name}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_name(@NotNull SQLiteSyntaxParser.Database_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#database_name}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_name(@NotNull SQLiteSyntaxParser.Database_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#module_argument}.
	 * @param ctx the parse tree
	 */
	void enterModule_argument(@NotNull SQLiteSyntaxParser.Module_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#module_argument}.
	 * @param ctx the parse tree
	 */
	void exitModule_argument(@NotNull SQLiteSyntaxParser.Module_argumentContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#join_operator}.
	 * @param ctx the parse tree
	 */
	void enterJoin_operator(@NotNull SQLiteSyntaxParser.Join_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#join_operator}.
	 * @param ctx the parse tree
	 */
	void exitJoin_operator(@NotNull SQLiteSyntaxParser.Join_operatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#vacuum_stmt}.
	 * @param ctx the parse tree
	 */
	void enterVacuum_stmt(@NotNull SQLiteSyntaxParser.Vacuum_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#vacuum_stmt}.
	 * @param ctx the parse tree
	 */
	void exitVacuum_stmt(@NotNull SQLiteSyntaxParser.Vacuum_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#factored_select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFactored_select_stmt(@NotNull SQLiteSyntaxParser.Factored_select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#factored_select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFactored_select_stmt(@NotNull SQLiteSyntaxParser.Factored_select_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#transaction_name}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_name(@NotNull SQLiteSyntaxParser.Transaction_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#transaction_name}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_name(@NotNull SQLiteSyntaxParser.Transaction_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#cte_table_name}.
	 * @param ctx the parse tree
	 */
	void enterCte_table_name(@NotNull SQLiteSyntaxParser.Cte_table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#cte_table_name}.
	 * @param ctx the parse tree
	 */
	void exitCte_table_name(@NotNull SQLiteSyntaxParser.Cte_table_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void enterUnary_operator(@NotNull SQLiteSyntaxParser.Unary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void exitUnary_operator(@NotNull SQLiteSyntaxParser.Unary_operatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#common_table_expression}.
	 * @param ctx the parse tree
	 */
	void enterCommon_table_expression(@NotNull SQLiteSyntaxParser.Common_table_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#common_table_expression}.
	 * @param ctx the parse tree
	 */
	void exitCommon_table_expression(@NotNull SQLiteSyntaxParser.Common_table_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#drop_trigger_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_trigger_stmt(@NotNull SQLiteSyntaxParser.Drop_trigger_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#drop_trigger_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_trigger_stmt(@NotNull SQLiteSyntaxParser.Drop_trigger_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#new_table_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_table_name(@NotNull SQLiteSyntaxParser.New_table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#new_table_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_table_name(@NotNull SQLiteSyntaxParser.New_table_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#type_name}.
	 * @param ctx the parse tree
	 */
	void enterType_name(@NotNull SQLiteSyntaxParser.Type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#type_name}.
	 * @param ctx the parse tree
	 */
	void exitType_name(@NotNull SQLiteSyntaxParser.Type_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void enterTable_alias(@NotNull SQLiteSyntaxParser.Table_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void exitTable_alias(@NotNull SQLiteSyntaxParser.Table_aliasContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#module_name}.
	 * @param ctx the parse tree
	 */
	void enterModule_name(@NotNull SQLiteSyntaxParser.Module_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#module_name}.
	 * @param ctx the parse tree
	 */
	void exitModule_name(@NotNull SQLiteSyntaxParser.Module_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#commit_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCommit_stmt(@NotNull SQLiteSyntaxParser.Commit_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#commit_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCommit_stmt(@NotNull SQLiteSyntaxParser.Commit_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#trigger_name}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_name(@NotNull SQLiteSyntaxParser.Trigger_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#trigger_name}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_name(@NotNull SQLiteSyntaxParser.Trigger_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#create_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_stmt(@NotNull SQLiteSyntaxParser.Create_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#create_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_stmt(@NotNull SQLiteSyntaxParser.Create_table_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#pragma_value}.
	 * @param ctx the parse tree
	 */
	void enterPragma_value(@NotNull SQLiteSyntaxParser.Pragma_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#pragma_value}.
	 * @param ctx the parse tree
	 */
	void exitPragma_value(@NotNull SQLiteSyntaxParser.Pragma_valueContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#column_constraint}.
	 * @param ctx the parse tree
	 */
	void enterColumn_constraint(@NotNull SQLiteSyntaxParser.Column_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#column_constraint}.
	 * @param ctx the parse tree
	 */
	void exitColumn_constraint(@NotNull SQLiteSyntaxParser.Column_constraintContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#savepoint_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSavepoint_stmt(@NotNull SQLiteSyntaxParser.Savepoint_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#savepoint_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSavepoint_stmt(@NotNull SQLiteSyntaxParser.Savepoint_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#detach_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDetach_stmt(@NotNull SQLiteSyntaxParser.Detach_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#detach_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDetach_stmt(@NotNull SQLiteSyntaxParser.Detach_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#column_def}.
	 * @param ctx the parse tree
	 */
	void enterColumn_def(@NotNull SQLiteSyntaxParser.Column_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#column_def}.
	 * @param ctx the parse tree
	 */
	void exitColumn_def(@NotNull SQLiteSyntaxParser.Column_defContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#table_constraint}.
	 * @param ctx the parse tree
	 */
	void enterTable_constraint(@NotNull SQLiteSyntaxParser.Table_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#table_constraint}.
	 * @param ctx the parse tree
	 */
	void exitTable_constraint(@NotNull SQLiteSyntaxParser.Table_constraintContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void enterColumn_alias(@NotNull SQLiteSyntaxParser.Column_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void exitColumn_alias(@NotNull SQLiteSyntaxParser.Column_aliasContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDelete_stmt(@NotNull SQLiteSyntaxParser.Delete_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDelete_stmt(@NotNull SQLiteSyntaxParser.Delete_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#release_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRelease_stmt(@NotNull SQLiteSyntaxParser.Release_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#release_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRelease_stmt(@NotNull SQLiteSyntaxParser.Release_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#foreign_table}.
	 * @param ctx the parse tree
	 */
	void enterForeign_table(@NotNull SQLiteSyntaxParser.Foreign_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#foreign_table}.
	 * @param ctx the parse tree
	 */
	void exitForeign_table(@NotNull SQLiteSyntaxParser.Foreign_tableContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_view_stmt(@NotNull SQLiteSyntaxParser.Create_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_view_stmt(@NotNull SQLiteSyntaxParser.Create_view_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(@NotNull SQLiteSyntaxParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(@NotNull SQLiteSyntaxParser.ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#signed_number}.
	 * @param ctx the parse tree
	 */
	void enterSigned_number(@NotNull SQLiteSyntaxParser.Signed_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#signed_number}.
	 * @param ctx the parse tree
	 */
	void exitSigned_number(@NotNull SQLiteSyntaxParser.Signed_numberContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#delete_stmt_limited}.
	 * @param ctx the parse tree
	 */
	void enterDelete_stmt_limited(@NotNull SQLiteSyntaxParser.Delete_stmt_limitedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#delete_stmt_limited}.
	 * @param ctx the parse tree
	 */
	void exitDelete_stmt_limited(@NotNull SQLiteSyntaxParser.Delete_stmt_limitedContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#index_name}.
	 * @param ctx the parse tree
	 */
	void enterIndex_name(@NotNull SQLiteSyntaxParser.Index_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#index_name}.
	 * @param ctx the parse tree
	 */
	void exitIndex_name(@NotNull SQLiteSyntaxParser.Index_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSelect_stmt(@NotNull SQLiteSyntaxParser.Select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSelect_stmt(@NotNull SQLiteSyntaxParser.Select_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#create_virtual_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_virtual_table_stmt(@NotNull SQLiteSyntaxParser.Create_virtual_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#create_virtual_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_virtual_table_stmt(@NotNull SQLiteSyntaxParser.Create_virtual_table_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_stmt(@NotNull SQLiteSyntaxParser.Alter_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_stmt(@NotNull SQLiteSyntaxParser.Alter_table_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#attach_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAttach_stmt(@NotNull SQLiteSyntaxParser.Attach_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#attach_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAttach_stmt(@NotNull SQLiteSyntaxParser.Attach_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#pragma_stmt}.
	 * @param ctx the parse tree
	 */
	void enterPragma_stmt(@NotNull SQLiteSyntaxParser.Pragma_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#pragma_stmt}.
	 * @param ctx the parse tree
	 */
	void exitPragma_stmt(@NotNull SQLiteSyntaxParser.Pragma_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(@NotNull SQLiteSyntaxParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(@NotNull SQLiteSyntaxParser.Table_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(@NotNull SQLiteSyntaxParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(@NotNull SQLiteSyntaxParser.NameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#join_constraint}.
	 * @param ctx the parse tree
	 */
	void enterJoin_constraint(@NotNull SQLiteSyntaxParser.Join_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#join_constraint}.
	 * @param ctx the parse tree
	 */
	void exitJoin_constraint(@NotNull SQLiteSyntaxParser.Join_constraintContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#reindex_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReindex_stmt(@NotNull SQLiteSyntaxParser.Reindex_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#reindex_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReindex_stmt(@NotNull SQLiteSyntaxParser.Reindex_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(@NotNull SQLiteSyntaxParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(@NotNull SQLiteSyntaxParser.ParseContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#begin_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBegin_stmt(@NotNull SQLiteSyntaxParser.Begin_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#begin_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBegin_stmt(@NotNull SQLiteSyntaxParser.Begin_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#table_or_subquery}.
	 * @param ctx the parse tree
	 */
	void enterTable_or_subquery(@NotNull SQLiteSyntaxParser.Table_or_subqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#table_or_subquery}.
	 * @param ctx the parse tree
	 */
	void exitTable_or_subquery(@NotNull SQLiteSyntaxParser.Table_or_subqueryContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#error}.
	 * @param ctx the parse tree
	 */
	void enterError(@NotNull SQLiteSyntaxParser.ErrorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#error}.
	 * @param ctx the parse tree
	 */
	void exitError(@NotNull SQLiteSyntaxParser.ErrorContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#ordering_term}.
	 * @param ctx the parse tree
	 */
	void enterOrdering_term(@NotNull SQLiteSyntaxParser.Ordering_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#ordering_term}.
	 * @param ctx the parse tree
	 */
	void exitOrdering_term(@NotNull SQLiteSyntaxParser.Ordering_termContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(@NotNull SQLiteSyntaxParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(@NotNull SQLiteSyntaxParser.KeywordContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_clause(@NotNull SQLiteSyntaxParser.With_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_clause(@NotNull SQLiteSyntaxParser.With_clauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#pragma_name}.
	 * @param ctx the parse tree
	 */
	void enterPragma_name(@NotNull SQLiteSyntaxParser.Pragma_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#pragma_name}.
	 * @param ctx the parse tree
	 */
	void exitPragma_name(@NotNull SQLiteSyntaxParser.Pragma_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#select_or_values}.
	 * @param ctx the parse tree
	 */
	void enterSelect_or_values(@NotNull SQLiteSyntaxParser.Select_or_valuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#select_or_values}.
	 * @param ctx the parse tree
	 */
	void exitSelect_or_values(@NotNull SQLiteSyntaxParser.Select_or_valuesContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#simple_select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSimple_select_stmt(@NotNull SQLiteSyntaxParser.Simple_select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#simple_select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSimple_select_stmt(@NotNull SQLiteSyntaxParser.Simple_select_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#view_name}.
	 * @param ctx the parse tree
	 */
	void enterView_name(@NotNull SQLiteSyntaxParser.View_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#view_name}.
	 * @param ctx the parse tree
	 */
	void exitView_name(@NotNull SQLiteSyntaxParser.View_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void enterJoin_clause(@NotNull SQLiteSyntaxParser.Join_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void exitJoin_clause(@NotNull SQLiteSyntaxParser.Join_clauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterForeign_key_clause(@NotNull SQLiteSyntaxParser.Foreign_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitForeign_key_clause(@NotNull SQLiteSyntaxParser.Foreign_key_clauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_value(@NotNull SQLiteSyntaxParser.Literal_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_value(@NotNull SQLiteSyntaxParser.Literal_valueContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#drop_index_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_index_stmt(@NotNull SQLiteSyntaxParser.Drop_index_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#drop_index_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_index_stmt(@NotNull SQLiteSyntaxParser.Drop_index_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#compound_select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCompound_select_stmt(@NotNull SQLiteSyntaxParser.Compound_select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#compound_select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCompound_select_stmt(@NotNull SQLiteSyntaxParser.Compound_select_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#update_stmt_limited}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_stmt_limited(@NotNull SQLiteSyntaxParser.Update_stmt_limitedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#update_stmt_limited}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_stmt_limited(@NotNull SQLiteSyntaxParser.Update_stmt_limitedContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#collation_name}.
	 * @param ctx the parse tree
	 */
	void enterCollation_name(@NotNull SQLiteSyntaxParser.Collation_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#collation_name}.
	 * @param ctx the parse tree
	 */
	void exitCollation_name(@NotNull SQLiteSyntaxParser.Collation_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_stmt(@NotNull SQLiteSyntaxParser.Update_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_stmt(@NotNull SQLiteSyntaxParser.Update_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#drop_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_view_stmt(@NotNull SQLiteSyntaxParser.Drop_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#drop_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_view_stmt(@NotNull SQLiteSyntaxParser.Drop_view_stmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#raise_function}.
	 * @param ctx the parse tree
	 */
	void enterRaise_function(@NotNull SQLiteSyntaxParser.Raise_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#raise_function}.
	 * @param ctx the parse tree
	 */
	void exitRaise_function(@NotNull SQLiteSyntaxParser.Raise_functionContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#conflict_clause}.
	 * @param ctx the parse tree
	 */
	void enterConflict_clause(@NotNull SQLiteSyntaxParser.Conflict_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#conflict_clause}.
	 * @param ctx the parse tree
	 */
	void exitConflict_clause(@NotNull SQLiteSyntaxParser.Conflict_clauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link SQLiteSyntaxParser#analyze_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze_stmt(@NotNull SQLiteSyntaxParser.Analyze_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLiteSyntaxParser#analyze_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze_stmt(@NotNull SQLiteSyntaxParser.Analyze_stmtContext ctx);
}