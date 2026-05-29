import js from "@eslint/js";
import { defineConfig, globalIgnores } from "eslint/config";
import globals from "globals";

export default defineConfig([
  {
    files: ["**/*.{js,mjs,cjs}"],
    plugins: { js },
    extends: ["js/recommended"],
    languageOptions: { globals: globals.browser },
  },
  globalIgnores([
    "htmlReport/**",
    "build/**",
    "node_modules/**",
    "src/main/resources/static/js/**",
    "build/resources/main/static/js/**",
  ]),
]);
