# Imposters Web Game

A browser-based social deduction game inspired by games like Among Us, where players must identify the imposters hiding among the crew.

## How to Play

### Overview
Players are split into two teams: **Crewmates** and **Imposters**. Crewmates win by completing all tasks or voting out all imposters. Imposters win by eliminating enough crewmates or sabotaging the mission.

### Roles

| Role | Goal |
|------|------|
| **Crewmate** | Complete tasks and identify the imposters |
| **Imposter** | Eliminate crewmates and avoid being voted out |

### Game Flow

1. **Lobby** – Players join a room and the host starts the game.
2. **Task Phase** – Crewmates complete mini-tasks around the map. Imposters fake tasks and look for opportunities to eliminate crewmates.
3. **Emergency Meeting / Dead Body Report** – Any player can call a meeting to discuss and vote.
4. **Discussion** – Players discuss who they think the imposter(s) are.
5. **Voting** – Everyone votes to eject a player (or skips). The player with the most votes is ejected.
6. **Repeat** – Continue until a win condition is met.

### Win Conditions

- **Crewmates win** if all tasks are completed, or all imposters are voted out.
- **Imposters win** if they equal or outnumber the crewmates, or successfully complete a critical sabotage.

### Tips for Crewmates
- Watch other players' movements and note who is near locations where eliminations occur.
- Complete tasks quickly — a full task bar is an instant crewmate victory.
- Use Emergency Meetings wisely; don't call one without useful information.

### Tips for Imposters
- Blend in by pretending to complete tasks.
- Use sabotage strategically to split up crewmates or force a loss.
- Build trust before making a move.

## Getting Started (Development)

### Prerequisites
- A modern web browser (Chrome, Firefox, Edge, Safari)
- [Node.js](https://nodejs.org/) (for local development server)

### Running Locally

```bash
# Clone the repository
git clone https://github.com/Synkrotic/Imposters-web-game.git
cd Imposters-web-game

# Install dependencies
npm install

# Start the development server
npm start
```

Then open your browser and navigate to `http://localhost:3000`.

## Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) before submitting issues or pull requests.

## License

This project is open source. See [LICENSE](LICENSE) for details.