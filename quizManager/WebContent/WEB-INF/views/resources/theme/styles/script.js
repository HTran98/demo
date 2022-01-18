function addAnswer() {
	var dem = document.getElementsByClassName("hien").length;
	var an = document.getElementsByClassName("an");
	an[0].classList.add("hien");
	an[0].classList.remove("an");
}
function removeAnswer() {
	var hien = document.getElementsByClassName("hien")
	hien[hien.length-1].classList.add("an");
	hien[hien.length-1].classList.remove("hien");
}