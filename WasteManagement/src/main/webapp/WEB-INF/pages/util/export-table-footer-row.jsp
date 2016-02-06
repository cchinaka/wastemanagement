<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<tr class='export-data-row' data-reporttitle="${param.reportTitle}" data-rootaction="${param.rootAction}" data-tableselector="${param.tableSelector}" data-exportkey="${param.exportKey}">
	<td colspan='${param.colspan}' class='text-right'>
		<button class="btn btn-default btn-sm print-pdf-button btn-export"
			type="button" data-path="pdf">
			<i class="fa fa-file-pdf-o"></i> Export PDF
		</button>
		<button class="btn btn-default btn-sm print-excel-button btn-export"
			type="button" data-path="excel">
			<i class="fa fa-file-excel-o"></i> Export Excel
		</button>
	</td>
</tr>