const API_BASE = "/api/drivers";

document.addEventListener("DOMContentLoaded", () => {
  bindRacesButton();
});

function bindRacesButton() {
  const loadRacesButton = document.getElementById("loadRacesBtn");
  if (!loadRacesButton) return;

  loadRacesButton.addEventListener("click", async () => {
    const driverId = loadRacesButton.dataset.id;
    if (!driverId) return;
    await getDriversRaces(driverId);
  });
}

async function getDriversRaces(id) {
  try {
    const res = await fetch(`${API_BASE}/${id}/races`, {
      method: "GET",
      headers: {
        Accept: "application/json",
      },
    });

    if (!res.ok) {
      if (res.status === 403) {
        alert("You need to be logged in to view race history.");
      }
      return;
    }

    const races = await res.json();
    displayRacesTable(races);
  } catch (error) {
    console.error(error);
  }
}

function displayRacesTable(races) {
  const container = document.getElementById("racesContainer");
  if (!container) return;

  if (races.length === 0) {
    container.innerHTML = "<h3>This driver has not participated in any races</h3>";
    return;
  }

  container.innerHTML = `
        <table class="table table-striped table-hover align-middle">
            <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Date</th>
                    <th>Track</th>
                    <th>Winner</th>
                </tr>
            </thead>
            <tbody>
                ${
    races.map((race) => `
                    <tr>
                        <td>${race.id}</td>
                        <td>${race.name}</td>
                        <td>${race.date}</td>
                        <td>${race.trackName || ""}</td>
                        <td>${race.winnerName || ""}</td>
                    </tr>
                `).join("")
  }
            </tbody>
        </table>
    `;
}
