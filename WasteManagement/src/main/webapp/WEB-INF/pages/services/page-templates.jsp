<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<div class="modal fade email-action-modal" tabindex="-1"
	id="email-action-modal" role="dialog">
	<div class="vertical-alignment-helper">
		<div class="modal-dialog modal-lg vertical-align-center">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title">
						<i class='fa fa-database'></i> Setup Email Action
					</h4>
				</div>
				<div class="modal-body">
					<form class="form email-action-form" id="email-action-form">
						<div class="form-group">
							<label>Action Name</label> <input type="text"
								class="form-control required" name="emailActionName"
								placeholder="Enter an action name" /> <input type="hidden"
								name="emailActionType" /> <input type="hidden"
								name="emailActionModalId" /> <input type="hidden"
								name="emailActionId" />
						</div>

						<div class="form-group">
							<label>Email</label> <input type="text"
								class="form-control required" name="emailActionEmail"
								placeholder="Enter an email" />
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<i class="fa fa-times"></i> Close
					</button>
					<button type="button" class="btn btn-primary btn-save-db-action">
						<i class="fa fa-check"></i> Save Email Action
					</button>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade db-action-modal" tabindex="-1"
	id="db-action-modal" role="dialog">
	<div class="vertical-alignment-helper">
		<div class="modal-dialog modal-lg vertical-align-center">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title">
						<i class='fa fa-database'></i> Setup Database Action
					</h4>
				</div>
				<div class="modal-body">
					<form class="form db-action-form" id="db-action-form">
						<div class="row">
							<div class="col-sm-4">
								<div class="form-group">
									<label>Action Name</label> <input type="text"
										class="form-control required" name="dbActionName"
										placeholder="Enter an action name" /> <input type="hidden"
										name="dbActionType" /> <input type="hidden"
										name="dbActionModalId" /> <input type="hidden"
										name="dbActionId" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Database Type</label> <input type="text"
										class="form-control required" name="dbActionDatabaseType"
										placeholder="Enter a database type" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Database URL</label> <input type="text"
										class="form-control required" name="dbActionDatabaseUrl"
										placeholder="Enter a database Url" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Username</label> <input type="text"
										class="form-control username required" name="dbActionUsername"
										placeholder="Enter a user name" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Password</label> <input type="password"
										name="dbActionPassword" class="form-control"
										placeholder="Enter a password" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Confirm Password</label> <input type="password"
										name="dbActionConfirmPassword" class="form-control"
										placeholder="Please confirm the password" />
								</div>
							</div>
							<div class="col-sm-12">
								<div class="form-group">
									<label>Active</label><br /> <input type="checkbox"
										name="dbActionActive" class="switch" checked />
								</div>
							</div>
							<div class="col-sm-12">
								<div class="form-group">
									<label>Payment Instruction SQL</label>
									<textarea class="form-control required" row="20"
										name="dbActionSql" placeholder="Instruction SQL"></textarea>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<i class="fa fa-times"></i> Close
					</button>
					<button type="button" class="btn btn-primary btn-save-db-action">
						<i class="fa fa-check"></i> Save Database Action
					</button>
				</div>
			</div>
		</div>
	</div>
</div>



<div class="modal fade data-config-modal" tabindex="-1"
	id="data-config-modal" role="dialog">
	<div class="vertical-alignment-helper">
		<div class="modal-dialog modal-lg vertical-align-center">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title">
						<i class='fa fa-database'></i> Setup Data Config
					</h4>
				</div>
				<div class="modal-body">
					<form class="form data-config-form" id="data-config-form">
						<div class="row">
							<div class="col-sm-4">
								<div class="form-group">
									<label>Data Config Name</label> <input type="text"
										class="form-control action-name required" name="configName"
										placeholder="Enter an action name" /> <input type="hidden"
										name="configType" class="config-type" /> <input type="hidden"
										name="modalId" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Database Type</label> <input type="text"
										class="form-control database-type required"
										name="databaseType" placeholder="Enter a database type" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Database URL</label> <input type="text"
										class="form-control database-url required" name="databaseUrl"
										placeholder="Enter a database Url" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Username</label> <input type="text"
										class="form-control username required" name="username"
										placeholder="Enter a user name" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Password</label> <input type="password" name="password"
										class="form-control password" placeholder="Enter a password" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label>Confirm Password</label> <input type="password"
										name="confirmPassword" class="form-control"
										placeholder="Please confirm the password" />
								</div>
							</div>
							<div class="col-sm-12">
								<div class="form-group">
									<label>Active</label><br /> <input type="checkbox"
										name="active" class="switch" checked />
								</div>
							</div>
							<div class="col-sm-12">
								<div class="form-group">
									<label>Payment Instruction SQL</label>
									<textarea class="form-control required" row="20" name="sql"
										placeholder="Instruction SQL"></textarea>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<i class="fa fa-times"></i> Close
					</button>
					<button type="button" class="btn btn-primary btn-save-config">
						<i class="fa fa-check"></i> Save Data Config
					</button>
				</div>
			</div>
		</div>
	</div>
</div>





<div class="hidden" id="page-templates">
	<div class="row action-email-row">
		<div class="col-sm-6">
			<div class="form-group">
				<label><strong>Action Name</strong></label> <input type="text"
					class="form-control action-name" placeholder="Enter an action name" />
			</div>
			<div class="checkbox">
				<label><input type="checkbox" name="activeAction" class=""
					checked /> Active</label>
			</div>
		</div>
		<div class="col-sm-6">
			<div class="form-group">
				<label>Email</label> <input type="email" class="form-control"
					placeholder="Enter Email">
			</div>
			<div class="well well-sm">
				<button type="button" class="btn btn-danger btn-remove-row"
					data-parent-class="action-email-row">
					<i class="fa fa-minus-circle"></i> Remove
				</button>
			</div>
		</div>
	</div>
	<div class="row action-row">
		<div class="col-xs-12">
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
						<label><strong>Action Name</strong></label> <input type="text"
							class="form-control action-name"
							placeholder="Enter an action name" />
					</div>
					<div class="form-group">
						<label>Database Type</label> <input type="text"
							class="form-control database-type"
							placeholder="Enter a database type" />
					</div>
					<div class="form-group">
						<label>Database URL</label> <input type="text"
							class="form-control database-url"
							placeholder="Enter a database Url" />
					</div>
					<div class="form-group">
						<label>Username</label> <input type="text"
							class="form-control username" placeholder="Enter a user name" />
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Password</label> <input type="password"
									class="form-control password" placeholder="Enter a password" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Confirm Password</label> <input type="password"
									class="form-control password"
									placeholder="Please confirm the password" />
							</div>
						</div>
					</div>
					<div class="checkbox">
						<label><input type="checkbox" name="activeAction" class=""
							checked /> Active</label>
					</div>
				</div>
				<div class="col-sm-6">

					<div class="form-group">
						<label>Payment Instruction SQL</label>
						<textarea class="form-control" row="20"
							placeholder="Instruction SQL"></textarea>
					</div>
					<div class="well well-sm">
						<button type="button" class="btn">
							<i class="fa fa-life-buoy"></i> Test Connection
						</button>
						<button type="button" class="btn btn-danger btn-remove-row"
							data-parent-class="action-row">
							<i class="fa fa-minus-circle"></i> Remove
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xs-12">
			<hr />
		</div>
	</div>

	<table class="table-row-holder">
		<tbody>
			<tr class="income-data-row" data-modalid="">
				<td class='sn width1 text-center'></td>
				<td class='config-active width1 text-center'></td>
				<td class='name'></td>
				<td class='config-type'></td>
				<td class='database-type'></td>
				<td class='url'></td>
				<td class='tasks nowrap'>
					<button type="button" class="btn" title="test connection"
						data-modalid="">
						<i class="fa fa-exchange"></i>
					</button>
					<button type="button" class="btn btn-edit-row" title="edit record"
						data-modalid="">
						<i class="fa fa-edit"></i>
					</button>
					<button type="button" class="btn btn-danger btn-remove-row"
						title="delete record" data-parent-class="action-row"
						data-modalid="">
						<i class="fa fa-minus-circle"></i>
					</button>
				</td>
			</tr>

			<tr class="action-row" data-modalid="">
				<td class='sn width1 text-center'></td>
				<td class='config-active width1 text-center'></td>
				<td class='name'></td>
				<td class='action-type'></td>
				<td class='tasks nowrap'>
					<button type="button" class="btn" title="test connection"
						data-modalid="">
						<i class="fa fa-exchange"></i>
					</button>
					<button type="button" class="btn btn-edit-row" title="edit record"
						data-modalid="">
						<i class="fa fa-edit"></i>
					</button>
					<button type="button" class="btn btn-danger btn-remove-row"
						title="delete record" data-parent-class="action-row"
						data-modalid="">
						<i class="fa fa-minus-circle"></i>
					</button>
				</td>
			</tr>
		</tbody>
	</table>
</div>