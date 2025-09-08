import { defineConfig } from "vitest/config";

export default defineConfig({
    test: {
        environment: "jsdom",
        globals: true,
        setupFiles: "tests/setup.ts",
        coverage: {
            provider: "v8",
            reporter: ["text", "json", 'json-summary', "html"],
            reportsDirectory: "./tests/unit/coverage",
            reportOnFailure: true,
            thresholds: {
                lines: 70,
                branches: 70,
                functions: 70,
                statements: 70
            }
        }
    }
});
