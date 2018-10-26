/**
 * 
 */

function showDivHideDiv(showDiv,hideDiv){
	jQuery(hideDiv).hide();
	jQuery(showDiv).fadeIn(1500);
}

function hideDiv(idDiv){
	jQuery(idDiv).hide();
}

function showDiv(idDiv){
	jQuery(idDiv).show();
}

function hide(documentId){
	document.getElementById(documentId).style.display = 'none';
}
function show(documentId){
	document.getElementById(documentId).style.display = 'block';
}