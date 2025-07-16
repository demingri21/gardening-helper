let username = "";

function saveUsername() {
  const input = document.getElementById("username").value.trim();
  if (!input) { alert("Enter username"); return; }
  username = input;

  fetch("http://localhost:8080/setUsername", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username })
  })
  .then(res => res.text())
  .then(msg => {
    console.log(msg);
    document.getElementById("login").style.display = "none";
    document.getElementById("mainUI").style.display = "block";
    document.getElementById("displayUsername").innerText = username;
    getFlowers();
  })
  .catch(console.error);
}

function getFlowers() {
  fetch("http://localhost:8080/getFlowers")
    .then(res => res.json())
    .then(data => {
      const list = document.getElementById("flowersList");
      list.innerHTML = "";
      data.forEach(f => {
        const div = document.createElement("div");
        div.innerHTML = `
          <span>${f.FlowerName} – every ${f.WaterInterval}d, last watered ${f.daysSinceWatered}d ago</span>
          <button onclick="deleteFlower('${f.FlowerName}')">Delete</button>
        `;
        list.appendChild(div);
      });
    })
    .catch(console.error);
}


function addFlower() {
  const flowerName = document.getElementById("newFlowerName").value.trim();
  const waterInterval = parseInt(document.getElementById("waterInterval").value);
  const daysSinceWatered = parseInt(document.getElementById("daysSinceWatered").value);

  if (!flowerName || isNaN(waterInterval) || isNaN(daysSinceWatered)) {
    alert("Enter all flower details");
    return;
  }

  fetch("http://localhost:8080/addFlower", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      username, flowerName, waterInterval, daysSinceWatered
    })
  })
    .then(res => res.text())
    .then(msg => {
      alert(msg);
      getFlowers();
    })
    .catch(console.error);
}

function deleteFlower(flowerName) {
  fetch("http://localhost:8080/deleteFlower", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, flowerName })
  })
    .then(res => res.text())
    .then(msg => {
      alert(msg);
      getFlowers(); // Refresh the list after deletion
    })
    .catch(console.error);
}

