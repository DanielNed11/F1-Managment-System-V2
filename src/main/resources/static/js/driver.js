async function getAllDrivers() {
    try {
        const res = await fetch('http://localhost:8080/api/drivers',
            {
                method: "GET",
                headers: {
                    Accept: "application/json"
                }
            });

        if (res.ok) {
            return await res.json()
        }
    } catch (err) {
        console.error(err)
    }
}

async function getDriversRaces(id) {
    try {
        const res = await fetch(`http://localhost:8080/api/drivers/${id}/races`,
            {
                method: "GET",
                headers: {
                    Accept: "application/json"
                }
            });
        if (res.ok) {
            const races = await res.json();
            displayRacesTable(await races)
        }

    } catch (err) {
        console.error(err)
    }
}

async function deleteDriver(id) {
    if (!confirm("Are you sure you want to delete this driver?")) return
    try {
        const res = await fetch(`http://localhost:8080/api/drivers/${id}`,
            {
                method: "DELETE",
            }
        );

        if (res.ok) {
            alert('Driver deleted successfully!');
            const driverCard = document.querySelector(`[data-driver-id="${id}"]`);
            if (driverCard) {
                driverCard.remove();
                updateDriversCount();
            }
        } else if (res.status === 404) {
            alert('Driver not found');
        } else {
            alert('Failed to delete driver');
        }
    } catch (err) {
        console.error(err)
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("loadRacesBtn");
    if (btn) {
        btn.addEventListener("click", async () => {
            const driverId = btn.dataset.id;

            await getDriversRaces(driverId);
        });
    }

    const deleteButtons = document.querySelectorAll(".deleteDriverBtn");
    if (deleteButtons.length !== 0) {
        deleteButtons.forEach(btn => {
            btn.addEventListener("click", async () => {
                const driverId = btn.dataset.id;

                await deleteDriver(driverId);
            });
        });
    }
    
});

function displayRacesTable(races) {
    const container = document.getElementById("racesContainer");

    races.length === 0 ?
        container.innerHTML = `<h3>This driver has not participated in any races</h3>` :

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
                  ${races.map(race => `
                      <tr>
                          <td>${race.id}</td>
                          <td>${race.name}</td>
                          <td>${race.date}</td>
                          <td>${race.trackName || ''}</td>
                          <td>${race.winnerName || ''}</td>
                      </tr>
                  `).join('')}
              </tbody>
          </table>
      `;
}

function updateDriversCount() {
    const counter = document.getElementById("driversCount");
    if (!counter) return;

    const currentDrivers = document.querySelectorAll("[data-driver-id]").length;
    counter.textContent = String(currentDrivers);
}
